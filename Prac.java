package pro1;
import java.util.*;
import java.io.*;

public class Prac{

    public static int frequencyHolder[];
    public static int totalFrequency=0;
    private final String sourcePath="C:\\Users\\007ad\\OneDrive\\Desktop\\Adi\\javapl\\project\\test.txt";
    private final String destinationPath = "C:\\Users\\007ad\\OneDrive\\Desktop\\Adi\\javapl\\project";
    public static void main(String args[])throws Exception{
        Prac dc=new Prac();
        dc.countFrequency();
        dc.encode();
    }
    public void countFrequency()throws Exception{
        frequencyHolder=new int[256];
        try(FileReader fr=new FileReader(sourcePath);){
            int x;
            while((x=fr.read())!=-1){
                frequencyHolder[x]++;
                totalFrequency++;
            }
        }
    }
    public void encode()throws Exception{
        int n=frequencyHolder.length;
        PriorityQueue<Node> pq=new PriorityQueue<>(n,FREQUENCY_COMPARATOR);
        for(int i=0;i<n;i++){
            pq.add(new Node((char)i,frequencyHolder[i]));
        }
        while(!pq.isEmpty()){
        System.out.println(pq.peek());
        }
    
    }
    private static final Comparator<Node> FREQUENCY_COMPARATOR = (Node o1, Node o2) -> (int) (o1.getFrequency()-o2.getFrequency());
}