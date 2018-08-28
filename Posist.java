/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package posist;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;
import java.security.MessageDigest;
import sun.security.provider.SHA;

/**
 *
 * @author Aayushi Sachdev
 */
class Owner
{ String OwnerID;
  String OwnerName;
  double value1;
  String Hash;
  
  
   Owner(String OId, String OName, double value) {
        this.OwnerID = OId;
        this.OwnerName = OName;
        this.value1 = value;
    }
  public String getHash()throws NoSuchAlgorithmException
  {     Random rnd=new Random();
        int Salt1 = 100000 + rnd.nextInt(900000);  //  6 digit salt value generated for hashing 
        String hash =this.OwnerID+ this.OwnerName + this.value1+ Salt1;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] out1 = md.digest(hash.getBytes());
        return javax.xml.bind.DatatypeConverter.printHexBinary(out1);
    }
}


class Record {
    String timestamp;
    Owner data;
private static final AtomicInteger nodeNumber=new AtomicInteger(0); //Unique  Incremental integral Value
String nodeId= UUID.randomUUID().toString(); //uniquely identifies a String
String referenceNodeId;//Stores address of parent node
String childReferenceNodeId;//Stores addresses of children
String genesisReferenceNodeId;//Stores address of Genesis Node
String HashValue;//Stores Hash Value of the Set{timestamp,data,nodeNumber,nodeId,referenceNodeID,ChildReferenceNodeID,genesisReferenceNodeId}


 Record(String time, Owner data, String parentId){
        this.timestamp = time;
        this.data = data;
        this.referenceNodeId = parentId;
    }
    
 
  Record(String time, Owner data, String genesisId, String parentId){
        this.timestamp = time;
        this.data = data;
        this.genesisReferenceNodeId = genesisId;
        this.referenceNodeId = parentId;
    }
    
 public String getHash() throws NoSuchAlgorithmException
 { 
 
        Random rnd = new Random();
        int Salt1 = 100000 + rnd.nextInt(900000);  // Generating 6 digit salt for hashing 
        String hash = this.timestamp + this.data + this.nodeId + 
                this.referenceNodeId + this.childReferenceNodeId + this.genesisReferenceNodeId + Salt1;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] out = md.digest(hash.getBytes());
        return javax.xml.bind.DatatypeConverter.printHexBinary(out);
 
 
 
 }



}
public class Posist {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException{
        Scanner s=new Scanner(System.in);
        System.out.println("Enter Owner ID");
        String OID=s.nextLine();
        System.out.println("Enter new Owner Name");
        String OName=s.nextLine();
        System.out.println("Enter the New Data Value");
        double dataValue=s.nextDouble();
        Owner root=new Owner(OID,OName,dataValue);
        root.Hash= root.getHash();
        String genesisParentId=null;
        long time=System.currentTimeMillis();
        DecimalFormat df = new DecimalFormat("#.00");
        df.format(dataValue);
        String epoch = Long.toString(time);
        Record node = new Record(epoch, root, genesisParentId);
        int nodeId = System.identityHashCode(node); 
        String genesisRefNodeId = Integer.toString(nodeId);
        node.genesisReferenceNodeId = genesisRefNodeId;
        node.HashValue = node.getHash();
        System.out.println("Enter New Owner ID");
        String c1oid = s.nextLine();
        System.out.println("Enter New Owner Name");
        String c1oname = s.nextLine();
        System.out.println("Enter New data value");
        double c1dataValue = s.nextDouble();
        df.format(c1dataValue);
        Owner node1 = new Owner(c1oid, c1oname, c1dataValue);
        node1.Hash = node1.getHash();
        Record node_1 = new Record(epoch, node1, genesisRefNodeId, genesisRefNodeId);
        node_1.HashValue = node1.getHash();
        System.out.println("Enter New Owner ID");
        String node2 = s.nextLine();
        System.out.println("Enter New Owner Name");
        String c2oname = s.nextLine();
        System.out.println("Enter New data value");
        double c2dataValue = s.nextDouble();
        df.format(c2dataValue);
        Owner node_2 = new Owner(node2, c2oname, c2dataValue);
        node_2.Hash = node_2.getHash();
        Record node3 = new Record(epoch, node_2, genesisRefNodeId, genesisRefNodeId);  //Node 2 with parent as genesis
        node3.HashValue = node3.getHash();      
    }
}
