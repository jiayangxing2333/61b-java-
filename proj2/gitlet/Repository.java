package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File stageFolder = join(GITLET_DIR, "stageFolder");

    public static final File removeFolder  = join(GITLET_DIR, "removeFolder");

    public static final File commitFolder = join(GITLET_DIR, "commitFolder");

    public static final File blobFolder = join(GITLET_DIR, "blobFolder");

    public static final File stageInform = join(GITLET_DIR, "stageInform");



    //private HashMap<String, Commit> commitFile = new HashMap<>();
    //private ArrayList<String> commitIdList = new ArrayList<>();
    //private HashSet<File> stageFiles = new HashSet<>();
    //private HashSet<File> waitForStage = new HashSet<>();



    /* TODO: fill in the rest of this class. */
    public static void checkGitDir(){
        if (!GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void init(){
        if ( !GITLET_DIR.exists()){
            GITLET_DIR.mkdir();
            stageFolder.mkdir();
            commitFolder.mkdir();
            blobFolder.mkdir();
            removeFolder.mkdir();
            stageInform.mkdir();
            Commit initialCommit = new Commit("initial commit", new Date(0));
            initialCommit.setBranchName("master");
            initialCommit.setParent("null");
            stage currentStage = new stage();
            currentStage.setCurrentBranch("master");
            currentStage.setCommitStageSha1(initialCommit.getCommitID());
            currentStage.saveStage();
            initialCommit.saveCommit();
            //HashMap< String, byte[]> stageFiles = new HashMap< String, byte[]>();
            //Utils.writeObject(stageFolder, stageFiles);
        }else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    // ** problem ** we need to check the specific text, not stage for addition this whole file
                  // how to check if args[1] in the cwd
    public static void add(String text){
        File currentFile = Utils.join( CWD, text);
        File stageFile = Utils.join( stageFolder, text);
        if ( !currentFile.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }

        stage currentStage = stage.readStage();
        Commit currentCommit = Commit.readCommit(currentStage.getCommitStageSha1());

        String content = Utils.readContentsAsString(currentFile);
        String textSha1 = Utils.sha1(text, content);
        List<String > stageFilesList = Utils.plainFilenamesIn(stageFolder);
        for (String stageFileName : stageFilesList){
            if (currentCommit.getCommitFiles().containsKey(stageFileName)){
                if (textSha1.equals(currentCommit.getCommitFiles().get(stageFileName))){
                    stageFile.delete();
                }
            }
        }
        if ( !stageFilesList.contains(text)){
            Utils.writeContents(stageFile, content);
        }else if ( stageFilesList.contains(text)){
            String stageContent = Utils.readContentsAsString( stageFile);
            if ( ! content.equals(stageContent)){
                Utils.writeContents( stageFile,content);
            }
        }
    }

    public static Commit commit(String text ){
        stage currentStage = stage.readStage();
        Date date = new Date();
        Commit newCommit = new Commit(text, date);
        Commit previousCommit = Commit.readCommit(currentStage.getCommitStageSha1());
        newCommit.setParent(previousCommit.getCommitID());
        currentStage.setCommitStageSha1(newCommit.getCommitID());
        List<String> stageFiles = Utils.plainFilenamesIn(stageFolder);
        if (previousCommit.getCommitFiles() != null){
            for( String i : previousCommit.getCommitFiles().keySet()){
                newCommit.getCommitFiles().put(i, previousCommit.getCommitFiles().get(i));
            }
        }

        if (stageFiles.size() == 0 && currentStage.getFilesWaitRemove().isEmpty()){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        for (String fileNames : stageFiles){
            File checkFile = Utils.join(stageFolder, fileNames);
            String checkContent = Utils.readContentsAsString(checkFile);
            String blobSha1 = Utils.sha1( fileNames, checkContent );
            File blobFile = Utils.join(blobFolder, blobSha1);
            if( newCommit.getCommitFiles() != null){
                if (newCommit.getCommitFiles().containsKey(fileNames)){
                    if ( ! blobSha1.equals(newCommit.getCommitFiles().get(fileNames))){
                        newCommit.getCommitFiles().remove(fileNames);
                        newCommit.getCommitFiles().put(fileNames, blobSha1);
                        Utils.writeContents(blobFile, checkContent);
                        checkFile.delete();
                    }
                }else if ( !newCommit.getCommitFiles().containsKey(fileNames)){
                    newCommit.getCommitFiles().put(fileNames, blobSha1);
                    Utils.writeContents(blobFile, checkContent);
                    checkFile.delete();
                }
            }else{
                newCommit.saveFiles(fileNames, blobSha1);
                Utils.writeContents(blobFile, checkContent);
                checkFile.delete();
            }

        }
        if (currentStage.getFilesWaitRemove() != null){
            for (String removeFile : currentStage.getFilesWaitRemove().keySet()){
                if (newCommit.getCommitFiles()!=null){
                    if ( newCommit.getCommitFiles().containsKey(removeFile)){
                        newCommit.getCommitFiles().remove(removeFile);
                    }
                    File currentRemoveFile = Utils.join( removeFolder, removeFile);
                    currentRemoveFile.delete();
                }
            }
        }

        newCommit.saveCommit();
        currentStage.clearStage();
        currentStage.saveStage();
        return newCommit;
    }

    public static void rm( String text){
    //No reason to remove the file.
        File currentFile = Utils.join(CWD, text);
        String content = Utils.readContentsAsString(currentFile);
        stage currentStage = stage.readStage();
        Commit currentCommit = Commit.readCommit(currentStage.getCommitStageSha1());

        List<String> stageFiles = Utils.plainFilenamesIn(stageFolder);
        if (!stageFiles.contains(text) && !currentCommit.getCommitFiles().containsKey(text)){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (stageFiles.contains(text)){
            File thisFile = Utils.join(stageFolder, text);
            thisFile.delete();
        }
        if (currentCommit.getCommitFiles().containsKey(text)){
            currentStage.getFilesWaitRemove().put(text, content);
            File currentRemoveFile = Utils.join(CWD, text);
            currentRemoveFile.delete();
        }
        currentStage.saveStage();

    }

    public static void log(){
        stage currentStage = stage.readStage();
        Commit currentCommit = Commit.readCommit(currentStage.getCommitStageSha1());
        while ( !currentCommit.getParent().equals("null")){
            currentCommit.display();
            currentCommit = Commit.readCommit(currentCommit.getParent());
        }
        currentCommit.display();

    }

    public static void globalLog( ){
        List<String > allCommits = Utils.plainFilenamesIn(commitFolder);
        for (String i : allCommits){
            Commit  thisCommit = Commit.readCommit(i);
            thisCommit.display();
        }

    }

    public static void find( String text){
        boolean check = false;
        List<String> allCommits = Utils.plainFilenamesIn(commitFolder);
        for (String i : allCommits){
            Commit thisCommit = Commit.readCommit(i);
            if (thisCommit.getMessage().equals(text)){
                System.out.println(thisCommit.getCommitID());
                check = true;
            }
        }
        if ( !check ){
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status(){
        stage currentStage = stage.readStage();
        System.out.println("=== Branches ===");
        if (currentStage.getCurrentBranch().equals("master")){
            System.out.println("*master");
        }else{
            System.out.println("master");
        }
        System.out.println();
        System.out.println("=== Stage Files ===");
        List<String > stageFiles = Utils.plainFilenamesIn(stageFolder);
        for (String i : stageFiles){
            System.out.println(i);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        List<String > removeFiles = Utils.plainFilenamesIn(removeFolder);
        for (String i : removeFiles){
            System.out.println(i);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void checkout( String[] args){
        if (args.length == 3 && args[1].equals("--")){
            String checkFile = args[2];
            stage currentStage = stage.readStage();
            Commit currentCommit = Commit.readCommit(currentStage.getCommitStageSha1());
            if ( !currentCommit.getCommitFiles().containsKey(checkFile)){
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }else{
                File cwdFile = Utils.join(CWD, checkFile);
                File blobFile = Utils.join(blobFolder, currentCommit.getCommitFiles().get(checkFile));
                Utils.writeContents(cwdFile, readContentsAsString(blobFile));
            }
            currentStage.saveStage();
        }else if(args.length == 4 && args[2].equals("--")){
            List<String > commitIDs = Utils.plainFilenamesIn( commitFolder);
            if( !commitIDs.contains(args[1])){
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }else{
                Commit checkCommit = Commit.readCommit(args[1]);
                if ( !checkCommit.getCommitFiles().containsKey(args[3])){
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                }else{
                    File cwdFile2 = Utils.join(CWD, args[3]);
                    File blobFile2 = Utils.join(blobFolder, checkCommit.getCommitFiles().get(args[3]));
                    Utils.writeContents(cwdFile2, readContentsAsString(blobFile2));
                }
            }
        }else if( args.length == 2){

        }
    }

    public static void branch( String text){

    }

    public static void rmBranch( String text){

    }

    public static void reset( int id){

    }

    public static void merge( String text){

    }




}
