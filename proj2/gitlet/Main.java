package gitlet;
import java.io.File;
import java.util.Arrays;
import static gitlet.Utils.*;
import java.util.concurrent.locks.ReadWriteLock;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new GitletException(
                    String.format("Incorrect operands."));
        }
    }

    public static void validateNumArgsForCommit(String cmd, String[] args, int n) {
        if (args.length == 1) {
            throw new GitletException(
                    String.format("Please enter a commit message."));
        }
    }

    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                Repository.checkGitDir();
                Repository.add(args[1]);
                break;
            case "commit":
                validateNumArgsForCommit( "commit", args, 1);
                validateNumArgs("commit", args, 2);
                // ignore everything outside the .gitlet directory entirely
                Repository.commit(args[1]);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.checkGitDir();
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.checkGitDir();
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.checkGitDir();
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.checkGitDir();
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.checkGitDir();
                Repository.status();
                break;
            case "checkout":
                Repository.checkout(args);
                break;
            case "branch":
                Repository.checkGitDir();
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.checkGitDir();
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                Repository.checkGitDir();
                Repository.reset(Integer.parseInt(args[1]));
                break;
            case "merge":
                Repository.checkGitDir();
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
