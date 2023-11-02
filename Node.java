package pro1;
public class Node{
    public char ch;
    private long freq;
    private Node lChild,rChild;
    private String code;

    Node(){;}
     Node(char ch,int f){
        this.ch=ch;
        this.freq=f;
        this.lChild=null;
        this.rChild=null;
    } 
    public Node(char ch,long freq,Node left,Node right){
        this.ch=ch;
        this.freq=freq;
        this.lChild=left;
        this.rChild=right;
    }
    public char getCharacter(){
        return ch;
    }
    public void setCharacter(char ch){
        this.ch=ch;
    }
    public long getFrequency(){
        return freq;
    }
    public void setFrequency(long fr){
        this.freq=fr;
    }
    public Node getrChild(){
        return rChild;
    }
    public void setrChild(Node rChild){
        this.rChild=rChild;
    }
    public Node getlChild(){
        return lChild;
    }
    public void setlChild(Node lChild){
        this.lChild=lChild;
    }    
    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code=code;
    }
}