package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/** Represents a dog that can be serialized.
 * @author Sean Dooher
*/
public class Dog {

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = new File("dogs.txt");

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        _age = age;
        _breed = breed;
        _name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        String regex = name + ".txt";
        File capdir = new File("./capDIR/dogDIR");
        File[] tempList = capdir.listFiles();

        if (tempList != null) {
            for (File file : tempList) {
                if (file.getName().equals(regex)) {
                    ArrayList<String> mydog = Utils.readObject(file, ArrayList.class);
                    return new Dog(mydog.get(0), mydog.get(1), Integer.parseInt(mydog.get(2)));
                }
            }
        }
        return null;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        _age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        ArrayList<String> savetmp = new ArrayList<String>();
        savetmp.add(_name);
        savetmp.add(_breed);
        savetmp.add(String.valueOf(_age));
        File Dogtmp = new File("./capDIR/dogDIR/" + _name + ".txt");
        Utils.writeObject(Dogtmp, savetmp);
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            _name, _breed, _age);
    }

    /** Age of dog. */
    private int _age;
    /** Breed of dog. */
    private String _breed;
    /** Name of dog. */
    private String _name;
}
