package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;

public class stage  implements Serializable{
    private HashMap<String, String> filesOnStage;
    private HashMap<String, String> filesWaitRemove;
    private String commitStageSha1;
    private String currentBranch;

    public String getCurrentBranch(){
        return currentBranch;
    }
    public void setCurrentBranch(String branch){
        currentBranch = branch;
    }

    public void setCommitStageSha1(String commitID){
        commitStageSha1 = commitID;
    }
    public String getCommitStageSha1(){
        return commitStageSha1;
    }

    public void setFilesOnStage(HashMap<String, String> filesOnStage ){
        this.filesOnStage = filesOnStage;
    }
    public HashMap<String, String> getFilesOnStage(){
        return filesOnStage;
    }
    public void saveStageFile(String name , String content){
        filesOnStage.put(name, content);
    }

    public void setFilesWaitRemove(HashMap<String, String> filesWaitRemove ){
        this.filesWaitRemove = filesWaitRemove;
    }
    public HashMap<String, String> getFilesWaitRemove(){
        return filesWaitRemove;
    }
    public void saveRemoveFile(String name , String content){
        filesWaitRemove.put(name, content);
    }

    public void clearStage(){
        setFilesOnStage(new HashMap<String, String>());
        setFilesWaitRemove(new HashMap<String, String>());
    }

    public void saveStage(){
        File stageFiles = Utils.join(Repository.stageInform, "currentStage");
        Utils.writeObject(stageFiles, this);
    }

    public static stage readStage(){
        File stageFiles = Utils.join(Repository.stageInform, "currentStage");
        return Utils.readObject(stageFiles, stage.class);
    }


}
