package gitlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.System.exit;

/** Commands from Main.
 * @author Jilin He
 * */
public class Commands {

    /** Current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Repository. */
    static final File MYGITLET = new File(".gitlet");
    /** Directory for staging. */
    private static File _staging = null;
    /** Directory for commits, each commit stores in ".txt" file
     * while files with commit store in directory in the name
     * of specific number. */
    private static File _commitState = null;
    /** File stores current head pointer. */
    private static File _head = null;
    /** File stores current head pointer's file path. */
    private static File _config = null;
    /** File stores all the branches' name. */
    private static File _logsBranches = null;
    /** Directory stores each blob. */
    private static File _objects = null;
    /** File for add region. */
    private static File _addStaging = null;
    /** File for remove region. */
    private static File _removeStaging = null;
    /** Directory for commits, named by commit's id. */
    private static File _commitIDs = null;
    /** Directory for each branch, in each of the branch file, contains
     * the path of the head. */
    private static File _branches = null;
    /** Directory of remote. */
    private static File _remotes = null;

    /** Helper function to get commit state.
     * @return commit state.
     * */
    public static File getCommitState() {
        return _commitState;
    }

    /** Helper function to get branch file.
     * @return branch file.
     * */
    public static File getBranches() {
        return _branches;
    }

    /** Init function. */
    public void init() {
        if (MYGITLET.exists()) {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
            exit(0);
        }
        setup();
        Commit initial = new Commit("initial commit", null, null, "master");
        String filepath = initial.saveCommit(null);
        Utils.writeContents(_head, "master");
        Utils.writeContents(_config, filepath);
        Utils.writeContents(_logsBranches, "master");
        File temp = Utils.join(_commitIDs, initial.getCommitId());
        Utils.writeContents(temp, filepath);
        File temp1 = Utils.join(_branches, "master");
        Utils.writeContents(temp1, filepath);
    }

    /** Setup function is to create instances for repository. */
    public void setup() {
        MYGITLET.mkdir();
        _staging = Utils.join(MYGITLET, "staging");
        _head = Utils.join(MYGITLET, "HEAD");
        _config = Utils.join(MYGITLET, "config");
        _logsBranches = Utils.join(MYGITLET, "logs_Branches");
        _objects = Utils.join(MYGITLET, "objects");
        _commitState = Utils.join(MYGITLET, "commits");
        _commitIDs = Utils.join(MYGITLET, "commitIDs");
        _branches = Utils.join(MYGITLET, "branches");
        _remotes = Utils.join(MYGITLET, "remotes");

        _staging.mkdir();
        _objects.mkdir();
        _commitState.mkdir();
        _commitIDs.mkdir();
        _branches.mkdir();
        _remotes.mkdir();

        _addStaging = Utils.join(_staging, "add");
        _removeStaging = Utils.join(_staging, "remove");
    }

    /** TestInit function is to test whether there's a repository,
     * if not, print 'Not in an initialized Gitlet directory.' and exit. */
    public void testInit() {
        File temp = new File(".gitlet");
        if (!temp.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            exit(0);
        }
    }

    /** FindCommStateWithID function is to find commit state's path by
     * giving commit id.
     * @param commID is the commit id.
     * @return commit state's path. */
    public String findCommStateWithID(String commID) {
        File[] commiid = _commitIDs.listFiles();
        String commCont = null;
        if (commiid != null) {
            for (File comm : commiid) {
                if (comm.getName().equals(commID)) {
                    commCont = Utils.readContentsAsString(comm);
                    break;
                }
            }
        }
        return commCont;
    }

    /** FindCommDirWIthCommState function is to find commit files by
     * giving commit state's path.
     * @param commpath is commit state's path.
     * @return directory of this commit. */
    public File[] findCommDirWithCommState(String commpath) {
        String[] readContents = commpath.split("/");
        String[] readLast =
                readContents[readContents.length - 1].split("\\.");
        String readSucc = readLast[0];
        File readDir = Utils.join(_commitState, readSucc);
        return readDir.listFiles();
    }

    /** Add function.
     * @param filename is the file name. */
    public void add(String filename) {
        File addFile = Utils.join(CWD, filename);
        if (addFile.exists()) {
            File removeFile = Utils.join(_staging, "remove");
            if (removeFile.length() != 0) {
                String[] rmFiles =
                        Utils.readContentsAsString(removeFile).split("\n");
                for (String rmfile: rmFiles) {
                    if (!rmfile.equals("") && rmfile.equals(filename)) {
                        clear(new File(_removeStaging.getPath()));
                        exit(0);
                    }
                }
            }
            Blob addblob = new Blob();
            addblob.blobAdd(addFile);
            Commit retrieveLast =
                    fromParental(Utils.readContentsAsString(_config));
            String commCont =
                    findCommStateWithID(retrieveLast.getCommitId());
            if (commCont != null) {
                File[] files1 = findCommDirWithCommState(commCont);
                if (files1 != null) {
                    for (File temp : files1) {
                        if (temp.getName().equals(filename)) {
                            String bfSha =
                                    Utils.readContentsAsString(temp);
                            String fSha =
                                    Utils.readContentsAsString(addFile);
                            if (bfSha.equals(fSha)) {
                                return;
                            }
                        }
                    }
                }
            }
            File blobfile = Utils.join(_objects, filename);
            Utils.writeObject(blobfile, addblob);
            File addfile = Utils.join(_staging, "add");
            String allfilename = null;
            if (addfile.exists()) {
                allfilename = Utils.readContentsAsString(addfile);
                allfilename = allfilename + "\n" + filename;
                Utils.writeContents(addfile, allfilename);
            } else {
                Utils.writeContents(addfile, filename);
            }
            return;
        }
        System.out.println("File does not exist.");
        exit(0);
    }

    /** GetCommDirNum function is to get the number of the commits.
     * @return number of the commits at present. */
    public int getCommDirNum() {
        File workingDir = new File(_commitState.getPath());
        File[] tempList = workingDir.listFiles();
        int len = 1;
        if (tempList != null) {
            for (File temp1 : tempList) {
                len += temp1.isFile() ? 0 : 1;
            }
        }
        return len;
    }

    /** CommitCheck function is to check the failure cases for commit.
     * @param message is the message in a commit. */
    public void commitCheck(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message. ");
            exit(0);
        }
        if (_addStaging.length() == 0 && _removeStaging.length() == 0) {
            System.out.println("No changes added to the commit.");
            exit(0);
        }
    }

    /** HelpCommitAdd function is a helper function for Commit when
     * dealing with add region.
     * @param message is the message in this commit.
     * @param commDir is the commit directory. */
    public void helpCommitAdd(String message, File commDir) {
        String commAddString =
                Utils.readContentsAsString(_addStaging);
        String[] commAddList = commAddString.split("\n");
        String[] commBlob = new String[commAddList.length];
        Commit parent =
                fromParental(Utils.readContentsAsString(_config));
        String parentState = findCommStateWithID(parent.getCommitId());
        File[] parentDir = findCommDirWithCommState(parentState);
        if (parentDir != null) {
            for (File pf: parentDir) {
                String parentContent = Utils.readContentsAsString(pf);
                File cf = Utils.join(commDir, pf.getName());
                Utils.writeContents(cf, parentContent);
            }
        }
        String current = Utils.readContentsAsString(_head); int i = 0;
        for (String temp : commAddList) {
            if (!temp.equals("")) {
                File prevFile = Utils.join(CWD, temp);
                String prevFileCont = Utils.readContentsAsString(prevFile);
                File postFile = Utils.join(commDir, temp);
                Utils.writeContents(postFile, prevFileCont);
                File postFile2 = Utils.join(commDir, temp);
                Utils.writeContents(postFile2, prevFileCont);
                File blobFile = Utils.join(_objects, temp);
                Blob temp1 = Utils.readObject(blobFile, Blob.class);
                commBlob[i++] = temp1.getFileid();
                commAddString.concat(temp1.getFileid());
                commAddString.concat(temp1.getFilename());
                commAddString.concat(temp1.getFilepath());
            }
        }
        int n = 1; Commit[] parent1 = new Commit[n];
        Pattern r = Pattern.compile("^Merged (.*) (.*) into (.*) (.*).$");
        Matcher m = r.matcher(message);
        if (m.find()) {
            n = 2; parent1 = new Commit[n];
            File temp = new File(Utils.readContentsAsString(_config));
            Commit p1 = Utils.readObject(temp, Commit.class);
            String commstate2 = findCommStateWithID(m.group(1));
            Commit p2 = Utils.readObject(new File(commstate2), Commit.class);
            parent1[0] = p1;
            parent1[1] = p2;
        } else {
            File temp = new File(Utils.readContentsAsString(_config));
            Commit p1 = Utils.readObject(temp, Commit.class);
            parent1[0] = p1;
        }
        Commit comm = new Commit(message, parent1, commAddString, current);
        String filepath = comm.saveCommit(commBlob);
        Utils.writeContents(_config, filepath);
        File temp2 = Utils.join(_commitIDs, comm.getCommitId());
        Utils.writeContents(temp2, filepath);
        File temp3 = Utils.join(_branches, current);
        Utils.writeContents(temp3, filepath);
        File file = new File(_addStaging.getPath());
        clear(file);
    }

    /** Commit function.
     * @param message is the message in this commit. */
    public void commit(String message) {
        commitCheck(message);
        File commDir =
                Utils.join(_commitState, String.valueOf(getCommDirNum()));
        commDir.mkdir();

        if (_addStaging.exists()) {
            helpCommitAdd(message, commDir);
        }

        if (_removeStaging.exists()) {
            String commRmString = Utils.readContentsAsString(_removeStaging);
            String[] commrmlist = commRmString.split("\n");

            for (String temp: commrmlist) {
                if (!temp.equals("")) {
                    File currentbranchfile = Utils.join(commDir, temp);
                    if (currentbranchfile.exists()) {
                        currentbranchfile.delete();
                    }
                }
            }
            File file1 = new File(_removeStaging.getPath());
            clear(file1);
        }

    }

    /** Clear method is to clear a file.
     * @param file is the one needs to be cleared. */
    public void clear(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Remove function.
     * @param filename is file name. */
    public void remove(String filename) {
        boolean flag = false;
        if (_addStaging.length() != 0) {
            String[] filenames =
                    Utils.readContentsAsString(_addStaging).split("\n");
            String writeNames = null;
            for (String filed : filenames) {
                if (!filename.equals(filed)) {
                    if (writeNames == null) {
                        writeNames = filed;
                    } else {
                        writeNames = writeNames + ("\n" + filed);
                    }
                } else {
                    flag = true;
                }
            }
            Utils.writeContents(_addStaging, writeNames);
        }
        Commit retrieveLast =
                fromParental(Utils.readContentsAsString(_config));
        String commCont = findCommStateWithID(retrieveLast.getCommitId());
        if (commCont != null) {
            File[] files1 = findCommDirWithCommState(commCont);
            if (files1 != null) {
                for (File temp : files1) {
                    if (temp.getName().equals(filename)) {
                        flag = true;
                        File rmFile = Utils.join(CWD, filename);
                        rmFile.delete();
                        Utils.writeContents(_removeStaging, filename);
                        break;
                    }
                }
            }
        }
        if (!flag) {
            System.out.println("No reason to remove the file.");
            exit(0);
        }
    }

    /** Log function. */
    public void log() {
        String branch = Utils.readContentsAsString(_head);
        ArrayList<Commit> retrieveAll = Commit.fromfile(branch);
        for (Commit temp : retrieveAll) {
            System.out.println("===");
            String commitid = temp.getCommitId();
            String message = temp.getMessage();
            String timestamp = temp.getTimestamp();

            Pattern r = Pattern.compile("^Merged (.*) (.*) into (.*) (.*).$");
            Matcher m = r.matcher(message);
            if (m.find()) {
                System.out.println("commit " + commitid
                        + "\nDate: " + timestamp + "\nMerged " + m.group(2)
                        + " into " + m.group(4) + ".\n");
            } else {
                System.out.println("commit "
                        + commitid + "\nDate: "
                        + timestamp + "\n" + message + "\n");
            }
        }

    }

    /** Global log function. */
    public void globalLog() {
        ArrayList<Commit> retrieveAll = Commit.fromfile(null);
        for (Commit temp : retrieveAll) {
            System.out.println("===");
            String commitid = temp.getCommitId();
            String message = temp.getMessage();
            String timestamp = temp.getTimestamp();

            Pattern r = Pattern.compile("^Merged (.*) (.*) into (.*) (.*).$");
            Matcher m = r.matcher(message);
            if (m.find()) {
                System.out.println("commit " + commitid
                        + "\nDate: " + timestamp + "\nMerged " + m.group(2)
                        + " into " + m.group(4) + ".\n");
            } else {
                System.out.println("commit "
                        + commitid + "\nDate: "
                        + timestamp + "\n" + message + "\n");
            }
        }
    }

    /** Find function.
     * Given a commit message, print out all the commits' id
     * with the same commit message.
     * @param message is the message with commits. */
    public void find(String message) {
        ArrayList<Commit> retrieveAll = Commit.fromfile(null);
        boolean running = false;
        for (Commit temp : retrieveAll) {
            if (temp.getMessage().equals(message)) {
                System.out.println(temp.getCommitId());
                running = true;
            }
        }
        if (!running) {
            System.out.println("Found no commit with that message.");
            exit(0);
        }
    }

    /** Status function. */
    public void status() {
        String[] branches =
                Utils.readContentsAsString(_logsBranches).split("\n");
        String headPointer = Utils.readContentsAsString(_head);
        System.out.println("=== Branches ===");
        System.out.println("*" + headPointer);
        for (String temp1 : branches) {
            if (!temp1.equals(headPointer) && !temp1.equals("")) {
                System.out.println(temp1);
            }
        }

        System.out.println("\n=== Staged Files ===");
        if (_addStaging.exists() && _addStaging.length() != 0) {
            String[] adds =
                    Utils.readContentsAsString(_addStaging).split("\n");
            for (String temp2 : adds) {
                if (!temp2.equals("")) {
                    System.out.println(temp2);
                }
            }
        }

        System.out.println("\n=== Removed Files ===");
        if (_removeStaging.exists() && _removeStaging.length() != 0) {
            String[] removes =
                    Utils.readContentsAsString(_removeStaging).split("\n");
            for (String temp3 : removes) {
                if (!temp3.equals("")) {
                    System.out.println(temp3);
                }
            }
        }

        ArrayList<File> modify = new ArrayList<File>();
        ArrayList<File> untrack = new ArrayList<File>();
        ArrayList<File> deleted = new ArrayList<File>();
        String head = Utils.readContentsAsString(_config);
        stateDetect(head, modify, untrack, deleted);

        System.out.println("\n=== Modifications Not Staged For Commit ===");
        for (File mf: modify) {
            System.out.println(mf.getName() + "(modified)");
        }
        for (File df: deleted) {
            System.out.println(df.getName() + "(deleted)");
        }
        System.out.println("\n=== Untracked Files ===");
        for (File uf: untrack) {
            System.out.println(uf.getName());
        }
    }

    /** StateDetect function is to detect state information.
     * @param commstate is path of the commit state.
     * @param modify is modified files.
     * @param untrack is untracked files.
     * @param delete is deleted files.*/
    public void stateDetect(String commstate, ArrayList<File> modify,
                            ArrayList<File> untrack, ArrayList<File> delete) {
        File[] commitFiles = findCommDirWithCommState(commstate);
        List<File> currFiles = new ArrayList<File>();
        if (commitFiles != null) {
            currFiles.addAll(Arrays.asList(commitFiles));
        }
        File[] cwdDirAll = CWD.listFiles();

        boolean untrackFlag = true;
        if (cwdDirAll != null) {
            if (commitFiles != null) {
                for (File wkfile : cwdDirAll) {
                    if (wkfile.isFile()) {
                        for (File tf : commitFiles) {
                            if (tf.getName().equals(wkfile.getName())) {
                                untrackFlag = false;
                                if (!Utils.readContentsAsString(wkfile).
                                        equals(Utils.
                                                readContentsAsString(tf))) {
                                    modify.add(wkfile);
                                }
                                break;
                            }
                        }
                        if (untrackFlag) {
                            untrack.add(wkfile);
                        } else {
                            untrackFlag = true;
                        }
                    }
                }
            }
        } else {
            if (commitFiles != null) {
                delete.addAll(Arrays.asList(commitFiles));
            }
        }

        helperCurr2Wk(commitFiles, untrack, delete);
        helperAddRm(modify, delete);
    }

    /** HelperCurr2wk function.
     * @param commitFiles is files in commit directory.
     * @param untrack is untracked files.
     * @param deleted is deleted files. */
    public void helperCurr2Wk(File[] commitFiles, ArrayList<File> untrack,
                              ArrayList<File> deleted) {
        File[] cwdDirAll = CWD.listFiles();
        boolean undeleted = false;
        if (commitFiles != null) {
            if (cwdDirAll != null) {
                for (File cf: commitFiles) {
                    for (File wk: cwdDirAll) {
                        if (wk.isFile()) {
                            if (cf.getName().equals(wk.getName())) {
                                undeleted = true;
                                break;
                            }
                        }
                    }
                    if (undeleted) {
                        undeleted = false;
                    } else {
                        if (!deleted.contains(cf)) {
                            deleted.add(cf);
                        }
                    }
                }
            } else {
                for (File cf: commitFiles) {
                    if (!deleted.contains(cf)) {
                        deleted.add(cf);
                    }
                }
            }
        } else {
            if (cwdDirAll != null && !_addStaging.exists()
                    && _addStaging.length() == 0) {
                for (File wk: cwdDirAll) {
                    if (wk.isFile() && !untrack.contains(wk)) {
                        untrack.add(wk);
                    }
                }
            }
        }
    }

    /** HelperAddRm function.
     * @param modify is modified files.
     * @param deleted is deleted files.
     * */
    public void helperAddRm(ArrayList<File> modify, ArrayList<File> deleted) {
        File[] cwdDirAll = CWD.listFiles();
        if (_addStaging.exists() && _addStaging.length() != 0) {
            String[] addFiles =
                    Utils.readContentsAsString(_addStaging).split("\n");
            for (String af: addFiles) {
                if (cwdDirAll != null) {
                    for (File wk: cwdDirAll) {
                        if (wk.isFile()) {
                            if (!af.equals("") && wk.getName().equals(af)) {
                                File addf = Utils.join(_objects, af);
                                Blob addfb = Utils.readObject(addf, Blob.class);
                                String wks = Utils.readContentsAsString(wk);
                                if (!addfb.getFileid().equals(Utils.sha1(wks))
                                        && !modify.contains(wk)) {
                                    modify.add(wk);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    File addf = Utils.join(_objects, af);
                    if (!deleted.contains(addf)) {
                        deleted.add(addf);
                    }
                }
            }
        }

        if (_removeStaging.exists() && _removeStaging.length() != 0) {
            String[] rmf =
                    Utils.readContentsAsString(_removeStaging).split("\n");
            for (String rmfs: rmf) {
                deleted.removeIf(df -> df.getName().equals(rmfs));
            }
        }
    }

    /** Checkout function, if i equals to 1, checkout file;
     * if not, checkout branch.
     * @param i is the marked integer.
     * @param fileName is file need to be checkout.*/
    public void checkout(int i, String fileName) {
        if (i == 1) {
            String temp1 = Utils.readContentsAsString(_config);
            File[] commitdir =
                    findCommDirWithCommState(temp1);
            if (commitdir != null) {
                for (File cf : commitdir) {
                    if (cf.getName().equals(fileName)) {
                        String wantedContent = Utils.readContentsAsString(cf);
                        File returnFile = Utils.join(CWD, fileName);
                        Utils.writeContents(returnFile, wantedContent);
                        exit(0);
                    }
                }
                System.out.println("File does not exist in that commit.");
                exit(0);
            } else {
                System.out.println("File does not exist in that commit.");
                exit(0);
            }
        } else {
            fileName = fileName.replace("/", "-");
            helpCheckout(fileName);
        }
    }

    /** Helper function for checkout.
     * @param branchName is the branch name. */
    public void helpCheckout(String branchName) {
        File[] wkFiles = CWD.listFiles();
        String current = Utils.readContentsAsString(_head);
        if (current.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            exit(0);
        }
        File branchDir = Utils.join(_branches, branchName);
        if (branchDir.exists()) {
            String commstate = Utils.readContentsAsString(branchDir);
            File[] commdir = findCommDirWithCommState(commstate);
            judge(commdir);
            if (commdir != null) {
                for (File efile: commdir) {
                    String wantedContent = Utils.readContentsAsString(efile);
                    File returnFile = Utils.join(CWD, efile.getName());
                    Utils.writeContents(returnFile, wantedContent);
                }
            }
            boolean flag = false;
            if (wkFiles != null) {
                for (File wkfile : wkFiles) {
                    if (commdir != null) {
                        for (File cfile : commdir) {
                            if (cfile.getName().equals(wkfile.getName())) {
                                flag = true;
                            }
                        }
                    }
                    if (flag) {
                        flag = false;
                        continue;
                    }
                    wkfile.delete();
                }
            }
            Utils.writeContents(_head, branchName);
            File currentBr = Utils.join(_branches, branchName);
            String br = Utils.readContentsAsString(currentBr);
            Utils.writeContents(_config, br);
        } else {
            System.out.println("No such branch exists.");
            exit(0);
        }
    }

    /** Checkout function for checking out a specific file in a
     * specific commit.
     * @param commID is commit id.
     * @param fileName is file name. */
    public void checkout(String commID, String fileName) {
        if (commID.length() < 10) {
            commID = short2Long(commID);
        }
        String commstate = findCommStateWithID(commID);
        if (commstate == null) {
            System.out.println("No commit with that id exists.");
            exit(0);
        }
        File[] commdir = findCommDirWithCommState(commstate);
        if (commdir != null) {
            for (File efile: commdir) {
                if (efile.getName().equals(fileName)) {
                    String wantedContent = Utils.readContentsAsString(efile);
                    File returnFile = Utils.join(CWD, fileName);
                    Utils.writeContents(returnFile, wantedContent);
                    return;
                }
            }
            System.out.println("File does not exist in that commit.");
            exit(0);
        }
    }

    /** MakeBranch function is to make a new branch.
     * @param bname is branch name. */
    public void makeBranch(String bname) {
        String branchName = Utils.readContentsAsString(_logsBranches);
        String[] branchNames = branchName.split("\n");
        for (String brName : branchNames) {
            if (brName.equals(bname)) {
                System.out.println("A branch with that name already exists.");
                exit(0);
            }
        }
        branchName += "\n" + bname;
        Utils.writeContents(_logsBranches, branchName);

        File temp = Utils.join(_branches, bname);
        String shareName = Utils.readContentsAsString(_head);
        File shareHead = Utils.join(_branches, shareName);
        String masterPath = Utils.readContentsAsString(shareHead);
        Utils.writeContents(temp, masterPath);
    }

    /** Removebranch function is to remove a branch.
     * @param branch is branch name. */
    public void removeBranch(String branch) {
        String currentBranch = Utils.readContentsAsString(_head);
        if (branch.equals(currentBranch)) {
            System.out.println("Cannot remove the current branch.");
            exit(0);
        }
        String[] branches =
                Utils.readContentsAsString(_logsBranches).split("\n");
        String headNames = null;
        boolean running = false;
        for (String br : branches) {
            if (br.equals(branch)) {
                running = true;
                File branchFile = Utils.join(_branches, branch);
                branchFile.delete();
                continue;
            }
            if (headNames == null) {
                headNames = br;
            } else {
                headNames += "\n" + br;
            }
        }
        if (!running) {
            System.out.println("A branch with that name does not exist.");
            exit(0);
        }
        Utils.writeContents(_logsBranches, headNames);
    }

    /** Reset function.
     * @param commid is commit id. */
    public void reset(String commid) {
        String commstate = findCommStateWithID(commid);
        if (commstate == null) {
            System.out.println("No commit with that id exists.");
            exit(0);
        }
        File[] commDir = findCommDirWithCommState(commstate);
        judge(commDir);
        if (commDir != null) {
            for (File efile : commDir) {
                String wantedContent = Utils.readContentsAsString(efile);
                File returnFile = Utils.join(CWD, efile.getName());
                Utils.writeContents(returnFile, wantedContent);
            }
        }
        File[] wkFiles = CWD.listFiles();
        boolean flag = false;
        if (wkFiles != null) {
            for (File wkfile: wkFiles) {
                if (commDir != null) {
                    for (File cfile: commDir) {
                        if (cfile.getName().equals(wkfile.getName())) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        flag = false;
                    } else {
                        wkfile.delete();
                    }
                } else {
                    wkfile.delete();
                }
            }
        }
        Commit comm = Utils.readObject(new File(commstate), Commit.class);
        Utils.writeContents(_head, comm.getBranch());
        Utils.writeContents(_config, commstate);
        File temp = Utils.join(_branches, comm.getBranch());
        Utils.writeContents(temp, commstate);
        File file = new File(_addStaging.getPath());
        clear(file);
        File file1 = new File(_removeStaging.getPath());
        clear(file1);
    }

    /** TestMerge function is to test validation before implementing merge.
     * @param brname is branch name.
     * */
    public void testMerge(String brname) {
        if (_addStaging.length() > 0 || _removeStaging.length() > 0) {
            System.out.println("You have uncommitted changes.");
            exit(0);
        }
        String[] branchesName =
                Utils.readContentsAsString(_logsBranches).split("\n");
        boolean flag = false;
        for (String brnm : branchesName) {
            if (brnm.equals(brname)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println("A branch with that name does not exist. ");
            exit(0);
        }
    }

    /** Modified files in given branch compared to splitpoint. */
    private ArrayList<File> modifiedFileGiven = new ArrayList<File>();
    /** Modified files in current branch compared to splitpoint. */
    private ArrayList<File> modifiedFileCurr = new ArrayList<File>();
    /** Added files in given branch compared to splitpoint. */
    private ArrayList<File> addedFileGiven = new ArrayList<File>();
    /** Added files in current branch compared to splitpoint. */
    private ArrayList<File> addededFileCurr = new ArrayList<File>();
    /** Deleted files in given branch compared to splitpoint. */
    private ArrayList<File> deleteFileGiven = new ArrayList<File>();
    /** Deleted files in current branch compared to splitpoint. */
    private ArrayList<File> deleteFileCurr = new ArrayList<File>();
    /** Unmodified files in current branch compared to splitpoint. */
    private ArrayList<File> unmodifiedCurr = new ArrayList<File>();

    /** ModifyFilesGivenSplit function is to find the modified files
     * comparing to given branch and split point.
     * @param givenBrFiles is files in given branch.
     * @param splitPtFiles is files in split point. */
    public void modifyFilesGivenSplit(File[] givenBrFiles,
                                      File[] splitPtFiles) {
        boolean breakpoint = false;
        if (givenBrFiles != null) {
            for (File gfile : givenBrFiles) {
                if (splitPtFiles != null) {
                    for (File sf : splitPtFiles) {
                        if (sf.getName().equals(gfile.getName())) {
                            breakpoint = true;
                            if (!Utils.readContentsAsString(gfile).
                                    equals(Utils.readContentsAsString(sf))) {
                                modifiedFileGiven.add(gfile);
                            }
                            break;
                        }
                    }
                    if (breakpoint) {
                        breakpoint = false;
                    } else {
                        addedFileGiven.add(gfile);
                    }
                } else {
                    addedFileGiven.add(gfile);
                }
            }
        }
    }

    /** ModifyFilesCurrSplit function is to find the modified files
     * comparing to current branch and split point.
     * @param currBrFiles is files in current branch.
     * @param splitPtFiles is files in split point. */
    public void modifyFilesCurrSplit(File[] currBrFiles,
                                     File[] splitPtFiles) {
        boolean breakpoint1 = false;
        if (currBrFiles != null) {
            for (File gfile : currBrFiles) {
                if (splitPtFiles != null) {
                    for (File sf : splitPtFiles) {
                        if (sf.getName().equals(gfile.getName())) {
                            breakpoint1 = true;
                            if (!Utils.readContentsAsString(gfile).
                                    equals(Utils.readContentsAsString(sf))) {
                                modifiedFileCurr.add(gfile);
                            }
                            break;
                        }
                    }
                    if (breakpoint1) {
                        breakpoint1 = false;
                    } else {
                        addededFileCurr.add(gfile);
                    }
                } else {
                    addededFileCurr.add(gfile);
                }
            }
        }
    }

    /** DeletedFilesGivenSplit function is to find the deleted files
     * comparing to given branch and split point.
     * @param givenBrFiles is files in given branch.
     * @param splitPtFiles is files in split point. */
    public void deletedFilesGivenSplit(File[] givenBrFiles,
                                       File[] splitPtFiles) {
        boolean deleteflag = true;
        if (splitPtFiles != null) {
            for (File spfile: splitPtFiles) {
                if (givenBrFiles != null) {
                    for (File gfile: givenBrFiles) {
                        if (gfile.getName().equals(spfile.getName())) {
                            deleteflag = false;
                            break;
                        }
                    }
                    if (deleteflag) {
                        deleteFileGiven.add(spfile);
                    } else {
                        deleteflag = true;
                    }
                } else {
                    deleteFileGiven.add(spfile);
                }
            }
        }
    }

    /** DeletedFilesCurrSplit function is to find the deleted files
     * comparing to current branch and split point.
     * @param currBrFiles is files in current branch.
     * @param splitPtFiles is files in split point. */
    public void deletedFilesCurrSplit(File[] currBrFiles,
                                      File[] splitPtFiles) {
        boolean deleteflag1 = true;
        if (splitPtFiles != null) {
            for (File spfile: splitPtFiles) {
                if (currBrFiles != null) {
                    for (File cfile: currBrFiles) {
                        if (cfile.getName().equals(spfile.getName())) {
                            deleteflag1 = false;
                            break;
                        }
                    }
                    if (deleteflag1) {
                        deleteFileCurr.add(spfile);
                    } else {
                        deleteflag1 = true;
                    }
                } else {
                    deleteFileCurr.add(spfile);
                }
            }
        }
    }

    /** Merge cases 1 to 3.
     * @param givenComm is given commit. */
    public void caseOne2Three(Commit givenComm) {
        int flaggy = 0;
        for (File mgfile : modifiedFileGiven) {
            for (File mcfile : modifiedFileCurr) {
                if (mgfile.getName().equals(mcfile.getName())) {
                    if (Utils.readContentsAsString(mgfile).
                            equals(Utils.readContentsAsString(mcfile))) {
                        flaggy = 1;
                    }
                    break;
                }
            }
            if (flaggy == 0) {
                checkout(givenComm.getCommitId(), mgfile.getName());
            } else {
                flaggy = 0;
            }
        }
    }

    /** Merge cases 4 to 5.
     * @param givenComm is given commit. */
    public void caseFour2Five(Commit givenComm) {
        int flappy = 0;
        for (File agfile : addedFileGiven) {
            for (File acfile : addededFileCurr) {
                if (agfile.getName().equals(acfile.getName())) {
                    flappy = 1;
                    break;
                }
            }
            if (flappy == 0) {
                checkout(givenComm.getCommitId(), agfile.getName());
            } else {
                flappy = 0;
            }
        }
    }

    /** UnmodifiedFilesCurrSplit function is to find same files in
     * both current branch and split point.
     * @param currBrFiles is files in current branch.
     * @param splitPtFiles is files in split point. */
    public void unmodifiedFilesCurrSplit(File[] currBrFiles,
                                         File[] splitPtFiles) {
        if (splitPtFiles != null) {
            for (File spfile: splitPtFiles) {
                if (currBrFiles != null) {
                    for (File umcfile : currBrFiles) {
                        if (umcfile.isFile()) {
                            String spCont = Utils.readContentsAsString(spfile);
                            String umcCont =
                                    Utils.readContentsAsString(umcfile);
                            if (spCont.equals(umcCont) && spfile.getName().
                                    equals(umcfile.getName())) {
                                unmodifiedCurr.add(spfile);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /** CaseSix is merge 6th case.
     * @param givenBrFiles is files in given branch. */
    public void caseSix(File[] givenBrFiles) {
        int greedy = 0;
        for (File umcfile : unmodifiedCurr) {
            if (givenBrFiles != null) {
                for (File absgfile : givenBrFiles) {
                    if (absgfile.getName().equals(umcfile.getName())) {
                        String umcStr = Utils.readContentsAsString(umcfile);
                        String absStr = Utils.readContentsAsString(absgfile);
                        if (umcStr.equals(absStr)) {
                            greedy = 1;
                            break;
                        }
                    }
                }
                if (greedy == 0) {
                    remove(umcfile.getName());
                } else {
                    greedy = 0;
                }
            } else {
                remove(umcfile.getName());
            }
        }
    }

    /** MergeConflict function is to test the conflicts during a merge. */
    public void mergeConflict() {
        for (File mcfile: modifiedFileCurr) {
            for (File mgfile: modifiedFileGiven) {
                if (mcfile.getName().equals(mgfile.getName())
                        && !Utils.readContentsAsString(mcfile).
                        equals(Utils.readContentsAsString(mgfile))) {
                    String mess = "<<<<<<< HEAD\n"
                            + Utils.readContentsAsString(mcfile)
                            + "=======\n" + Utils.readContentsAsString(mgfile)
                            + ">>>>>>>\n";
                    System.out.println("Encountered a merge conflict.");
                    File temp = Utils.join(CWD, mcfile.getName());
                    Utils.writeContents(temp, mess);
                    add(Utils.join(CWD, mcfile.getName()).getName());
                }
            }
        }
        for (File mgfile: modifiedFileGiven) {
            for (File dcfile: deleteFileCurr) {
                if (mgfile.getName().equals(dcfile.getName())) {
                    String mess = "<<<<<<< HEAD\n" + "=======\n"
                            + Utils.readContentsAsString(mgfile)
                            + ">>>>>>>\n";
                    System.out.println("Encountered a merge conflict.");
                    File temp = Utils.join(CWD, mgfile.getName());
                    Utils.writeContents(temp, mess);
                    add(mgfile.getName());
                }
            }
        }
        for (File mcfile: modifiedFileCurr) {
            for (File dgfile: deleteFileGiven) {
                if (mcfile.getName().equals(dgfile.getName())) {
                    String mess = "<<<<<<< HEAD\n"
                            + Utils.readContentsAsString(mcfile)
                            + "=======\n" + ">>>>>>>\n";
                    System.out.println("Encountered a merge conflict.");
                    File temp = Utils.join(CWD, mcfile.getName());
                    Utils.writeContents(temp, mess);
                    add(mcfile.getName());
                }
            }
        }
        for (File acfile: addededFileCurr) {
            for (File agfile: addedFileGiven) {
                if (acfile.getName().equals(agfile.getName())
                        && !Utils.readContentsAsString(acfile).
                        equals(Utils.readContentsAsString(agfile))) {
                    String mess = "<<<<<<< HEAD\n"
                            + Utils.readContentsAsString(acfile)
                            + "=======\n" + Utils.readContentsAsString(agfile)
                            + ">>>>>>>\n";
                    System.out.println("Encountered a merge conflict.");
                    File temp = Utils.join(CWD, acfile.getName());
                    Utils.writeContents(temp, mess);
                    add(acfile.getName());
                }
            }
        }
    }

    /** Merge function.
     * @param brname is branch name. */
    public void merge(String brname) {
        testMerge(brname);
        String curler = Utils.readContentsAsString(_head);
        if (brname.equals(curler)) {
            System.out.println("Cannot merge a branch with itself.");
            exit(0);
        }
        String readConfig = Utils.readContentsAsString(_config);
        File[] currBrFiles = findCommDirWithCommState(readConfig);
        File[] wkDir = CWD.listFiles();
        judge(currBrFiles);
        judge(wkDir);
        String givenBrCommState =
                Utils.readContentsAsString(Utils.join(_branches, brname));

        Commit givenComm =
                Utils.readObject(new File(givenBrCommState), Commit.class);
        Commit currComm = Utils.readObject(new File(readConfig), Commit.class);
        Commit splitpoint = findSplitPoint(givenComm, currComm, brname);
        if (splitpoint.getCommitId().equals(currComm.getCommitId())) {
            checkout(3, brname);
            System.out.println("Current branch fast-forwarded.");
            exit(0);
        }

        File[] givenBrFiles = findCommDirWithCommState(givenBrCommState);
        String splitcommstate = findCommStateWithID(splitpoint.getCommitId());
        File[] splitPtFiles = findCommDirWithCommState(splitcommstate);
        modifyFilesGivenSplit(givenBrFiles, splitPtFiles);
        modifyFilesCurrSplit(currBrFiles, splitPtFiles);
        deletedFilesGivenSplit(givenBrFiles, splitPtFiles);
        deletedFilesCurrSplit(currBrFiles, splitPtFiles);
        caseOne2Three(givenComm);
        caseFour2Five(givenComm);
        File[] currBrFiles2 = CWD.listFiles();
        unmodifiedFilesCurrSplit(currBrFiles2, splitPtFiles);
        caseSix(givenBrFiles);
        mergeConflict();
        String commMessage = "Merged " + givenComm.getCommitId()
                + " " + brname.replace("-", "/") + " into "
                + currComm.getCommitId() + " " + curler + ".";
        if (splitpoint.getCommitId().equals(givenComm.getCommitId())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            exit(0);
        }
        File[] afterMerge = CWD.listFiles();
        if (afterMerge != null) {
            for (File fl: afterMerge) {
                if (fl.isFile()) {
                    add(fl.getName());
                }
            }
        }
        commit(commMessage);
    }

    /** Method to find split point.
     * @param givenComm is given commit.
     * @param currComm is current commit.
     * @param brname is just a mark, useless.
     * @return split point.
     * */
    public Commit findSplitPoint(Commit givenComm,
                                 Commit currComm, String brname) {
        Commit gPointer = givenComm;
        Commit cPointer = currComm;

        parentPointerG.put(givenComm, 0);
        parentPointerC.put(currComm, 0);
        findGivenBrTree(givenComm, 0, brname);
        findCurrBrTree(currComm, 0, brname);

        Commit splitpoint = new Commit();
        int dist = 1000;
        while (true) {
            for (Commit pc: parentPointerC.keySet()) {
                for (Commit pg: parentPointerG.keySet()) {
                    if (pc.getCommitId().equals(pg.getCommitId())
                            && parentPointerC.get(pc) < dist) {
                        splitpoint = pc;
                        dist = parentPointerC.get(pc);
                    }
                }
            }
            return splitpoint;
        }
    }

    /** parentPointerG is used to store the parent information of
     * the given branch. Integer displays the distance between two
     * Commits. */
    private HashMap<Commit, Integer>
            parentPointerG = new HashMap<Commit, Integer>();
    /** parentPointerC is used to store the parent information of
     * the current branch. */
    private HashMap<Commit, Integer>
            parentPointerC = new HashMap<Commit, Integer>();

    /** FindGivenBrTree function is to find parent tree of given branch.
     * @param givenComm is given commit.
     * @param dist is distance between two commits.
     * @param brname is branch name.*/
    public void findGivenBrTree(Commit givenComm, int dist, String brname) {
        if (givenComm.getParent() == null) {
            return;
        }
        Commit[] givenCommParent = givenComm.getParent();
        for (int i = 0; i < givenCommParent.length; i++) {
            dist += 1;
            parentPointerG.put(givenCommParent[i], dist);
            findGivenBrTree(givenCommParent[i], dist, brname);
        }
    }

    /** FindCurrBrTree function is to find parent tree of given branch.
     * @param currComm is current commit.
     * @param dist is distance between two commits.
     * @param brname is branch name.*/
    public void findCurrBrTree(Commit currComm, int dist, String brname) {
        if (currComm.getParent() == null) {
            return;
        }
        Commit[] currCommParent = currComm.getParent();
        for (int i = 0; i < currCommParent.length; i++) {
            dist += 1;
            parentPointerC.put(currCommParent[i], dist);
            findCurrBrTree(currCommParent[i], dist, brname);
        }
    }

    /** FromParental function is to find the parent commit.
     * @param filepath is the path to last commit.
     * @return last commit.
     * */
    public Commit fromParental(String filepath) {
        File parentfile = new File(filepath);
        Commit parentCommit = Utils.readObject(parentfile, Commit.class);
        return parentCommit;
    }

    /** Judge function is to judge whether there exists invalid condition.
     * @param  comefiles is files in the commit directory.
     * */
    public void judge(File[] comefiles) {
        File[] wkFiles = CWD.listFiles();
        String[] readContents =
                Utils.readContentsAsString(_config).split("/");
        String[] readLast =
                readContents[readContents.length - 1].split("\\.");
        String readSucc = readLast[0];
        File readDir = new File(".gitlet/commits/" + readSucc);
        File[] prevFiles = readDir.listFiles();
        ArrayList<File> untracked = new ArrayList<File>();
        boolean running1 = false;

        if (prevFiles == null && wkFiles != null) {
            for (File wkfile : wkFiles) {
                if (wkfile.isFile()) {
                    untracked.add(wkfile);
                }
            }
        } else if (prevFiles != null && wkFiles != null) {
            for (File wkfile : wkFiles) {
                if (wkfile.isFile()) {
                    String wsha = Utils.readContentsAsString(wkfile);
                    for (File tfile : prevFiles) {
                        String tsha = Utils.readContentsAsString(tfile);
                        if (wkfile.getName().equals(tfile.getName())
                                && tsha.equals(wsha)) {
                            running1 = true;
                            break;
                        }
                    }
                    if (!running1) {
                        untracked.add(wkfile);
                    } else {
                        running1 = false;
                    }
                }
            }
        }

        if (comefiles != null && !untracked.isEmpty()) {
            for (File cmfile : comefiles) {
                if (cmfile.isFile()) {
                    for (File untr : untracked) {
                        if (cmfile.getName().equals(untr.getName())) {
                            System.out.println("There is an untracked "
                                    + "file in the way; delete it,"
                                    + " or add and commit it first.");
                            exit(0);
                        }
                    }
                }
            }
        }
    }

    /** Short2Long function is to convert short id into long id.
     * @param commid is the short commit id.
     * @return normal commit id.
     * */
    public String short2Long(String commid) {
        File[] commids = _commitIDs.listFiles();
        String id = null;
        if (commids != null) {
            for (File cmid : commids) {
                if (cmid.getName().contains(commid)) {
                    id =  cmid.getName();
                    break;
                }
            }
        }
        return id;
    }

    /** AddRemote function is to saves the given login
     * information under the given remote name.
     * @param remoteName is the name of remote.
     * @param nameofDIR is the name of branch.
     * */
    public void addRemote(String remoteName, String nameofDIR) {
        File[] remoteNames = _remotes.listFiles();
        if (remoteNames != null) {
            for (File rnf: remoteNames) {
                if (rnf.getName().equals(remoteName)) {
                    System.out.println("A remote with "
                            + "that name already exists.");
                    exit(0);
                }
            }
        }
        File remoteBrFile = Utils.join(_remotes, remoteName);
        Utils.writeContents(remoteBrFile, nameofDIR);
    }

    /** RmRemote function is to remove information
     *  associated with the given remote name.
     *  @param remoteName is name of the remote. */
    public void rmRemote(String remoteName) {
        File[] remoteNames = _remotes.listFiles();
        boolean flag = false;
        if (remoteNames != null) {
            for (File rnf: remoteNames) {
                if (rnf.getName().equals(remoteName)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                System.out.println("A remote with that name does not exist.");
                exit(0);
            }
        } else {
            System.out.println("A remote with that name does not exist.");
            exit(0);
        }

        File remoteFile = Utils.join(_remotes, remoteName);
        clear(remoteFile);
        remoteFile.delete();
    }

    /** Push function is to attempts to append the current
     * branch's commits to the end of the given branch
     * at the given remote.
     * @param remoteName is name of the remote.
     * @param remoteBranch is the branch of the remote. */
    public void push(String remoteName, String remoteBranch)
            throws IOException {
        File remoteFile = Utils.join(_remotes, remoteName);
        String remoteBranchDIR = Utils.readContentsAsString(remoteFile);
        File remoteDIR = new File(remoteBranchDIR);
        if (!remoteDIR.exists()) {
            System.out.println("Remote directory not found.");
            exit(0);
        }
        File remoteConfig = Utils.join(remoteDIR, "config");
        String[] remoteConfigConts =
                Utils.readContentsAsString(remoteConfig).split("/");
        String remoteCommPath = remoteConfigConts[1]
                + "/" + remoteConfigConts[2];
        File remoteHeadComm = Utils.join(remoteDIR, remoteCommPath);
        Commit remoteHead = Utils.readObject(remoteHeadComm, Commit.class);

        String localHead = Utils.readContentsAsString(_head);
        ArrayList<Commit> localComms = Commit.fromfile(localHead);
        ArrayList<Commit> copy = new ArrayList<Commit>();
        for (Commit temp: localComms) {
            if (temp.getCommitId().equals(remoteHead.getCommitId())) {
                String currentHeadPath = Utils.readContentsAsString(_config);
                File currentHeadFile = new File(currentHeadPath);
                Commit currentHead =
                        Utils.readObject(currentHeadFile, Commit.class);
                Collections.reverse(copy);
                helperPush(currentHead, copy, remoteDIR, remoteBranch);
                exit(0);
            }
            copy.add(temp);
        }
        System.out.println("Please pull down remote changes before pushing.");
        exit(0);
    }


    /** HelperPush function.
     * @param currComm is current commit in local repo.
     * @param copy is an Arraylist of Commits.
     * @param remoteDIR is directory of remote.
     * @param branchname is name of branch.
     * */
    public void helperPush(Commit currComm, ArrayList<Commit> copy,
                           File remoteDIR, String branchname)
            throws IOException {
        for (Commit temp: copy) {
            File commIdFrom = Utils.join(_commitIDs, temp.getCommitId());
            File commIdTo = Utils.join(remoteDIR, "commitIDs/"
                    + temp.getCommitId());
            Files.copy(commIdFrom.toPath(), commIdTo.toPath());
            String commPath = findCommStateWithID(temp.getCommitId());
            File commitStateFrom = new File(commPath);
            File commitStateTo = Utils.join(remoteDIR, "commits/"
                    + commitStateFrom.getName());
            Files.copy(commitStateFrom.toPath(), commitStateTo.toPath());
            String[] commPaths = commPath.split("/");
            String[] readLast =
                    commPaths[commPaths.length - 1].split("\\.");
            String readSucc = readLast[0];
            File commitsFrom = Utils.join(_commitState, readSucc);
            File commitsTo = Utils.join(remoteDIR, "commits/" + readSucc);
            Files.copy(commitsFrom.toPath(), commitsTo.toPath());
            File[] objectsFrom = commitsFrom.listFiles();
            if (objectsFrom != null) {
                for (File of: objectsFrom) {
                    File objectTo = Utils.join(remoteDIR, "objects/"
                            + "r" + of.getName());
                    File commitstateDIRTo = Utils.join(remoteDIR, "commits/"
                            + readSucc + "/" + of.getName());
                    Files.copy(of.toPath(), objectTo.toPath());
                    Files.copy(of.toPath(), commitstateDIRTo.toPath());
                }
            }
        }
        String branchFrom = findCommStateWithID(currComm.getCommitId());
        File branchTo = Utils.join(remoteDIR, "branches/" + branchname);
        Utils.writeContents(branchTo, branchFrom);
        File configTo = Utils.join(remoteDIR, "config");
        Utils.writeContents(configTo, branchFrom);
        String[] remoteWKs = remoteDIR.getPath().split("/");
        String remoteCWD = remoteWKs[0];
        for (int i = 1; i < remoteWKs.length - 1; i++) {
            remoteCWD = remoteCWD + "/" + remoteWKs[i];
        }
        File remoteWork = new File(remoteCWD);
        File[] workFiles = remoteWork.listFiles();
        if (workFiles != null) {
            for (File wk: workFiles) {
                if (wk.isFile()) {
                    wk.delete();
                }
            }
        }
        File[] localFiles = findCommDirWithCommState(branchFrom);
        if (localFiles != null) {
            for (File lf: localFiles) {
                File temp = Utils.join(remoteWork, lf.getName());
                Files.copy(lf.toPath(), temp.toPath());
            }
        }
    }

    /** Fetch function is to Brings down commits from
     * the remote Gitlet repository into the local
     * Gitlet repository.
     * @param remoteName is name of a remote.
     * @param remoteBranch is name of branch.*/
    public void fetch(String remoteName, String remoteBranch)
            throws IOException {
        File remoteFileLocal = Utils.join(_remotes, remoteName);
        String remoteInfo = Utils.readContentsAsString(remoteFileLocal);
        File remoteFile = new File(remoteInfo);
        if (!remoteFile.exists()) {
            System.out.println("Remote directory not found.");
            exit(0);
        }
        File remoteBranchFile = Utils.join(remoteFile, "logs_Branches");
        String[] remoteBrs =
                Utils.readContentsAsString(remoteBranchFile).split("\n");
        for (String rb: remoteBrs) {
            if (rb.equals(remoteBranch)) {
                String brname = remoteName + "-" + remoteBranch;
                File brFile = Utils.join(_branches, brname);
                if (brFile.exists()) {
                    brFile.delete(); String input = helperFetch(brname);
                    Utils.writeContents(_logsBranches, input);
                }
                makeBranch(brname);
                File remoteConfig = Utils.join(remoteFile, "config");
                String remoteHeadComm =
                        Utils.readContentsAsString(remoteConfig);
                String remoteHeadPath =
                        remoteHeadComm.replace(".gitlet/", "");
                File remoteCommFile = Utils.join(remoteFile, remoteHeadPath);
                Commit remoteComm =
                        Utils.readObject(remoteCommFile, Commit.class);
                File localComm = Utils.join(_commitState, "r"
                        + remoteCommFile.getName());
                File localCommFromRemote =
                        Utils.join(_commitIDs, remoteComm.getCommitId());
                Files.copy(remoteCommFile.toPath(), localComm.toPath());
                File remotebranchlocal = Utils.join(_branches, brname);
                Utils.writeContents(remotebranchlocal, localComm.getPath());
                Utils.writeContents(localCommFromRemote, localComm.getPath());
                String[] commPaths = remoteHeadComm.split("/");
                String[] readLast =
                        commPaths[commPaths.length - 1].split("\\.");
                String readSucc = readLast[0];
                File remoteCommDIR = Utils.join(remoteFile, "commits/"
                        + readSucc);
                File localCommDIR = Utils.join(_commitState, "r" + readSucc);
                Files.copy(remoteCommDIR.toPath(), localCommDIR.toPath());
                File[] fromRmDIR = remoteCommDIR.listFiles();
                if (fromRmDIR != null) {
                    for (File file: fromRmDIR) {
                        File toLocal = Utils.join(localCommDIR, file.getName());
                        Files.copy(file.toPath(), toLocal.toPath());
                    }
                }
                return;
            }
        }
        System.out.println("That remote does not have that branch.");
        exit(0);
    }

    /** HelperFetch funtion is delete the branch name
     * in logs_branches file.
     * @param brname is the branch name.
     * @return input is the string need to write in
     * logs_branch file.
     * */
    public String helperFetch(String brname) {
        String input = null;
        String[] logs = Utils.readContentsAsString(_logsBranches)
                .split("\n");
        for (String lg: logs) {
            if (!lg.equals(brname)) {
                if (input == null) {
                    input = lg;
                } else {
                    input += "\n" + lg;
                }
            }
        }
        return input;
    }

    /** Pull function is to Fetches branch
     * [remote name]/[remote branch name] as for the fetch
     * command, and then merges that fetch into
     * the current branch.
     * @param remoteName is name of remote.
     * @param remoteBranch is the branch of remote.*/
    public void pull(String remoteName, String remoteBranch)
            throws IOException {
        fetch(remoteName, remoteBranch);
        merge(remoteName + "-" + remoteBranch);
    }
}
