package com.hos.imagepro;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
public class objectDetectionClass {
    // used to load model and predict
    private Interpreter interpreter;
    //store all label in array
    private List<String> labellist;
    private int Input_Size;
    private int Pixel_Size=3; //RGB
    private int Image_Mean=0;
    private float Image_STD=255.0f;
    //use to initialize gpu in app
    private GpuDelegate gpuDelegate;
    private int height=0;
    private int width=0;

    objectDetectionClass(AssetManager assetManager,String modelPath,String labelPath,int inputSize) throws IOException{
        Input_Size=inputSize;
        //use to define gpu or cpu //no. of threads
        Interpreter.Options options=new Interpreter.Options();
        gpuDelegate=new GpuDelegate();
        options.addDelegate(gpuDelegate);
        options.setNumThreads(4); //according to phone
        //loading model
        interpreter=new Interpreter(loadModelFile(assetManager,modelPath),options);
        //load label
        labellist=loadLabelList(assetManager,labelPath);
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        //to store label
        List<String> labelList=new ArrayList<>();
        //create a new reader
        BufferedReader reader=new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        //loop through each line and store it to labellist
        while ((line=reader.readLine())!=null){
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException{
        //use to get description of file
        AssetFileDescriptor fileDescriptor=assetManager.openFd(modelPath);
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
    //create new Mat function
    public Mat recognizeImage(Mat mat_image){
        //Rotate original image by 90 degree get portrait fame
        Mat rotated_mat_image=new Mat();
        Core.flip(mat_image.t(),rotated_mat_image,1);
        //convert to bitmap
        Bitmap bitmap=null;
        bitmap=Bitmap.createBitmap(rotated_mat_image.cols(),rotated_mat_image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rotated_mat_image,bitmap);
        //defing height and wieght
        height=bitmap.getHeight();
        width=bitmap.getWidth();
        //scale the bitmap to input size model
        Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,Input_Size,Input_Size,false);
        //convert bitmap to bytebuffer as model input should be in it
        ByteBuffer byteBuffer=convertBitmapToByteBuffer(scaledBitmap);

        //defining output


        Object[] input=new Object[1];
        input[0]=byteBuffer;

        Map<Integer,Object> output_map=new TreeMap<>();
        //we create treemap of three array(boxes,scores,classes)
        float[][][]boxes=new float[1][10][4];
        //10:top 10 object detected
        //4:there coordinate in image
        float[][]scores=new float[1][10];
        //stores scores of 10 object
        float[][]classes=new float[1][10];
        //stores classes of 10 object

        //add it to object map
        output_map.put(0,boxes);
        output_map.put(1,classes);
        output_map.put(2,scores);

        //predict
        interpreter.runForMultipleInputsOutputs(input,output_map);
        Object value=output_map.get(0);
        Object Object_class=output_map.get(1);
        Object score=output_map.get(2);

        //loop through each object
        //as output has only 10 boxes
        for(int i=0;i<10;i++){
            float class_value=(float) Array.get(Array.get(Object_class,0),i);
            float score_value=(float) Array.get(Array.get(score,0),i);
            //threshold for score
            if(score_value>0.5){
                Object box1=Array.get(Array.get(value,0),i);
                //multiply it with orginal height and width of frame

                float top=(float) Array.get(box1,0)*height;
                float left=(float) Array.get(box1,1)*width;
                float bottom=(float) Array.get(box1,2)*height;
                float right=(float) Array.get(box1,3)*width;
                //draw rectangle                  //startPoint               //EndPoint                 //color
                Imgproc.rectangle(rotated_mat_image,new org.opencv.core.Point(left,top),new org.opencv.core.Point(right,bottom),new Scalar(255,155,155),2);
                //write text on frame              //name of object                 //position                      //color       //size
                Imgproc.putText(rotated_mat_image,labellist.get((int)class_value),new Point(left,top),3,1,new Scalar(100,100,100),2);
            }

        }



        Core.flip(rotated_mat_image.t(),mat_image,0);
        return mat_image;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap ) {
        ByteBuffer byteBuffer;
        int quant=0;
        int size_image=Input_Size;
        if(quant==0){
            byteBuffer=ByteBuffer.allocateDirect(1*size_image*size_image*3);

        }
        else{
            byteBuffer=ByteBuffer.allocateDirect(4*1*size_image*size_image*3);

        }
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intvalues=new int[size_image*size_image];
        bitmap.getPixels(intvalues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        int pixel=0;
        for(int i=0;i<size_image;++i){
            for (int j=0;j<size_image;++j){
                final int val=intvalues[pixel++];
                if(quant==0){
                    byteBuffer.put((byte)((val>>16)&0xFF));
                    byteBuffer.put((byte)((val>>8)&0xFF));
                    byteBuffer.put((byte)(val&0xFF));
                }
                else {
                    byteBuffer.putFloat((((val >> 16)& 0xFF))/255.0f);
                    byteBuffer.putFloat((((val >> 8)& 0xFF))/255.0f);
                    byteBuffer.putFloat((((val) & 0xFF))/255.0f);
                }
            }
        }
        return byteBuffer;
    }
}

