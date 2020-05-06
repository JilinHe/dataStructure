package gitlet;

import java.io.IOException;

import static java.lang.System.exit;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Jilin He
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        Commands command = new Commands();
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            exit(0);
        }
        if (args[0].equals("init")) {
            command.init();
        } else {
            command.testInit(); command.setup();
            switch (args[0]) {
            case "add":
                command.add(args[1]); break;
            case "commit":
                command.commit(args[1]); break;
            case "log":
                command.log(); break;
            case "global-log":
                command.globalLog(); break;
            case "rm":
                command.remove(args[1]); break;
            case "find":
                command.find(args[1]); break;
            case "status":
                command.status(); break;
            case "checkout" :
                getCheckout(command, args); break;
            case "branch":
                command.makeBranch(args[1]); break;
            case "rm-branch" :
                command.removeBranch(args[1]); break;
            case "reset" :
                command.reset(args[1]); break;
            case "merge" :
                command.merge(args[1]); break;
            case "add-remote" :
                command.addRemote(args[1], args[2]); break;
            case "rm-remote" :
                command.rmRemote(args[1]); break;
            case "push" :
                command.push(args[1], args[2]); break;
            case "fetch" :
                command.fetch(args[1], args[2]); break;
            case "pull" :
                command.pull(args[1], args[2]); break;
            default:
                System.out.println("No command with that name exists.");
                exit(0);
            }
        }
    }

    /** Helper function with checkout.
     * @param command receives commands.
     * @param args inputs variables. */
    public static void getCheckout(Commands command, String... args) {
        if (args.length == 2) {
            command.checkout(3, args[1]);
        } else if (args.length == 3) {
            if (args[1].equals("--")) {
                command.checkout(1, args[2]);
            } else {
                System.out.println("Incorrect operands.");
                exit(0);
            }
        } else if (args.length == 4) {
            if (args[2].equals("--")) {
                command.checkout(args[1], args[3]);
            } else {
                System.out.println("Incorrect operands.");
                exit(0);
            }
        } else {
            System.out.println("Incorrect operands.");
            exit(0);
        }
    }
}
