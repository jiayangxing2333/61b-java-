package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String parent;
    private String commitID;
    private String branchName;
    private HashMap<String , String> commitFiles = new HashMap<String, String>();

    public Commit(String message, Date date){
        this.message = message;
        SimpleDateFormat simpleDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z");
        this.timestamp = simpleDate.format(date);
        this.commitID = Utils.sha1( message, timestamp, parent,branchName);
    }
    public String getMessage(){
        return message;
    }
    public HashMap< String , String> getCommitFiles(){
        return commitFiles;
    }
    public void saveFiles(String fileName, String blobSha1){
        commitFiles.put(fileName, blobSha1);
    }
    public void removeFile(String fileName ){
        commitFiles.remove(fileName);
    }

    public void setParent( String parentID){
        this.parent = parentID;
    }
    public String getParent(){
        return parent;
    }

    public void setBranchName( String branchName){
        this.branchName = branchName;
    }

    public String getBranchName(){
        return branchName;
    }

    public String getCommitID(){
        return commitID;
    }

    public void display(){
        System.out.println("===");
        System.out.println("commit "+ commitID);
        System.out.println("Date: " + timestamp);
        System.out.println( message);
        System.out.println();
    }

    public void saveCommit(){
        File commitFile = Utils.join( Repository.commitFolder, commitID);
        Utils.writeObject(commitFile, this);
    }

    public static Commit readCommit(String commitID){
        File commitFile = Utils.join( Repository.commitFolder, commitID);
        return Utils.readObject(commitFile, Commit.class);
    }

}
