package com.mycompany.myapp.util;

import java.util.HashMap;
import java.util.Map;

public class SampleCodeUtil {

    private static final Map<Integer, String> javaSampleCodes = new HashMap<>();
    private static final Map<Integer, String> pythonSampleCodes = new HashMap<>();

    static {
        // Initialize Java sample codes
        javaSampleCodes.put(
            1,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Declaration of variables\n" +
            "        int a = 5;  // You can change the value of a to any number\n" +
            "        int b = 3;  // You can change the value of b to any number\n\n" +
            "        // Adding the variables\n" +
            "        int result = a + b;\n\n" +
            "        // Printing the result\n" +
            "        System.out.println(result);\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            2,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Using a for loop to print numbers from 1 through 10\n" +
            "        for (int number = 1; number <= 10; number++) {  // loop from 1 to 10\n" +
            "            System.out.println(number);\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            3,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Variable to represent the phase of the moon\n" +
            "        // true means the moon is shining (full moon), false means the moon is dark (new moon)\n" +
            "        boolean light = true;  // You can change this to false to see what happens\n\n" +
            "        // Using if-else statements to choose the correct path\n" +
            "        if (light) {\n" +
            "            System.out.println(\"The moon is shining. Choose the path to the right.\");\n" +
            "        } else {\n" +
            "            System.out.println(\"It's dark. Choose the path to the left.\");\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            4,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Testing the function with different flow rates\n" +
            "        System.out.println(controlWaterFlow(55));  // Output: The flow is too strong!\n" +
            "        System.out.println(controlWaterFlow(5));   // Output: The flow is too weak\n" +
            "        System.out.println(controlWaterFlow(25));  // Output: The flow is perfect!\n" +
            "    }\n\n" +
            "    public static String controlWaterFlow(int rate) {\n" +
            "        // Checking the flow rate and returning a message based on the condition\n" +
            "        if (rate > 50) {\n" +
            "            return \"The flow is too strong!\";\n" +
            "        } else if (rate < 10) {\n" +
            "            return \"The flow is too weak\";\n" +
            "        } else {\n" +
            "            return \"The flow is perfect!\";\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            5,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Array of artifact numbers\n" +
            "        int[] artifacts = {101, 203, 304, 405, 506};  // You can modify this array as needed\n\n" +
            "        // Testing the function with a target artifact number\n" +
            "        System.out.println(findArtifact(artifacts, 304));  // Output: Artifact 304 found at position 2\n" +
            "        System.out.println(findArtifact(artifacts, 999));  // Output: Artifact 999 not found in the array\n" +
            "    }\n\n" +
            "    public static String findArtifact(int[] artifacts, int targetArtifact) {\n" +
            "        // Searching for the target artifact in the array\n" +
            "        for (int index = 0; index < artifacts.length; index++) {\n" +
            "            if (artifacts[index] == targetArtifact) {\n" +
            "                return \"Artifact \" + targetArtifact + \" found at position \" + index;\n" +
            "            }\n" +
            "        }\n" +
            "        return \"Artifact \" + targetArtifact + \" not found in the array\";\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            6,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Example instantiation of a hero\n" +
            "        Hero exampleHero = new Hero(\"Example Hero\", 5, 3);\n" +
            "        exampleHero.displayHero();\n\n" +
            "        // Instantiating a hero with the name \"Marcus\", strength equal to 10, and intelligence equal to 1\n" +
            "        Hero marcus = new Hero(\"Marcus\", 10, 1);\n" +
            "        marcus.displayHero();\n" +
            "    }\n" +
            "}\n\n" +
            "class Hero {\n" +
            "    private String name;\n" +
            "    private int strength;\n" +
            "    private int intelligence;\n\n" +
            "    public Hero(String name, int strength, int intelligence) {\n" +
            "        this.name = name;\n" +
            "        this.strength = strength;\n" +
            "        this.intelligence = intelligence;\n" +
            "    }\n\n" +
            "    public void displayHero() {\n" +
            "        System.out.println(\"Hero Name: \" + this.name);\n" +
            "        System.out.println(\"Strength: \" + this.strength);\n" +
            "        System.out.println(\"Intelligence: \" + this.intelligence);\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            7,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Instantiating a hero\n" +
            "        Hero hero = new Hero(\"Wrong Name\", 5, 7);\n\n" +
            "        // 1. Change the name of the hero to Andrew\n" +
            "        hero.setName(\"Andrew\");\n\n" +
            "        // 2. Increase his skill by 10\n" +
            "        hero.setSkill(hero.getSkill() + 10);\n\n" +
            "        // Assuming n is already declared somewhere in your code\n" +
            "        int n = 10; // For example\n\n" +
            "        // 3. Modify the for loop to count from 1 to n (inclusive)\n" +
            "        for (int i = 1; i <= n; i++) {\n" +
            "            System.out.println(i);\n" +
            "        }\n\n" +
            "        // 4. Fix the if statement to check if skill is higher than intelligence\n" +
            "        if (hero.getSkill() > hero.getIntelligence()) {\n" +
            "            System.out.println(hero.getName() + \" has greater skill than intelligence.\");\n" +
            "        } else {\n" +
            "            System.out.println(hero.getName() + \"'s intelligence is greater than or equal to their skill.\");\n" +
            "        }\n\n" +
            "        // Displaying the hero details\n" +
            "        hero.displayHero();\n" +
            "    }\n" +
            "}\n\n" +
            "class Hero {\n" +
            "    private String name;\n" +
            "    private int skill;\n" +
            "    private int intelligence;\n\n" +
            "    public Hero(String name, int skill, int intelligence) {\n" +
            "        this.name = name;\n" +
            "        this.skill = skill;\n" +
            "        this.intelligence = intelligence;\n" +
            "    }\n\n" +
            "    public void setName(String name) {\n" +
            "        this.name = name;\n" +
            "    }\n\n" +
            "    public void setSkill(int skill) {\n" +
            "        this.skill = skill;\n" +
            "    }\n\n" +
            "    public void setIntelligence(int intelligence) {\n" +
            "        this.intelligence = intelligence;\n" +
            "    }\n\n" +
            "    public String getName() {\n" +
            "        return name;\n" +
            "    }\n\n" +
            "    public int getSkill() {\n" +
            "        return skill;\n" +
            "    }\n\n" +
            "    public int getIntelligence() {\n" +
            "        return intelligence;\n" +
            "    }\n\n" +
            "    public void displayHero() {\n" +
            "        System.out.println(\"Hero Name: \" + this.name);\n" +
            "        System.out.println(\"Skill: \" + this.skill);\n" +
            "        System.out.println(\"Intelligence: \" + this.intelligence);\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            8,
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Calculate and print the factorial of 5\n" +
            "        int result = factorial(5);\n" +
            "        System.out.println(\"Factorial of 5: \" + result);\n" +
            "    }\n\n" +
            "    public static int factorial(int n) {\n" +
            "        if (n == 0) {\n" +
            "            return 1;\n" +
            "        } else {\n" +
            "            return n * factorial(n - 1);\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        javaSampleCodes.put(
            9,
            "import java.util.Random;\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.List;\n\n" +
            "class Hero {\n" +
            "    String name;\n" +
            "    int health;\n" +
            "    int strength;\n\n" +
            "    Hero(String name, int health, int strength) {\n" +
            "        this.name = name;\n" +
            "        this.health = health;\n" +
            "        this.strength = strength;\n" +
            "    }\n" +
            "}\n\n" +
            "class Monster {\n" +
            "    String name;\n" +
            "    int health;\n" +
            "    int strength;\n\n" +
            "    Monster(String name, int health, int strength) {\n" +
            "        this.name = name;\n" +
            "        this.health = health;\n" +
            "        this.strength = strength;\n" +
            "    }\n" +
            "}\n\n" +
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Create a hero instance\n" +
            "        Hero hero = new Hero(\"Sir Lancelot\", 100, 15);\n\n" +
            "        // Create a list of monsters\n" +
            "        List<Monster> monsters = new ArrayList<>();\n" +
            "        monsters.add(new Monster(\"Goblin\", 30, 5));\n" +
            "        monsters.add(new Monster(\"Dragon\", 100, 20));\n" +
            "        monsters.add(new Monster(\"Troll\", 50, 10));\n\n" +
            "        // Battle the monster\n" +
            "        battleMonster(hero, monsters);\n\n" +
            "        System.out.println(\"Game over!\");\n" +
            "    }\n\n" +
            "    public static void battleMonster(Hero hero, List<Monster> monsters) {\n" +
            "        Random rand = new Random();\n" +
            "        Monster monster = monsters.get(rand.nextInt(monsters.size()));\n\n" +
            "        while (hero.health > 0 && monster.health > 0) {\n" +
            "            int heroAttack = rand.nextInt(hero.strength) + 1;\n" +
            "            int monsterAttack = rand.nextInt(monster.strength) + 1;\n\n" +
            "            monster.health -= heroAttack;\n" +
            "            if (monster.health <= 0) {\n" +
            "                System.out.println(\"Your hero defeated the monster!\");\n" +
            "                break;\n" +
            "            }\n\n" +
            "            hero.health -= monsterAttack;\n" +
            "            if (hero.health <= 0) {\n" +
            "                System.out.println(\"The monster defeated your hero!\");\n" +
            "                break;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"
        );

        // Initialize Python sample codes
        pythonSampleCodes.put(
            1,
            "#Declaration of variables\n" +
            "a = 5  # You can change the value of a to any number\n" +
            "b = 3  # You can change the value of b to any number\n" +
            "\n" +
            "# Adding the variables\n" +
            "result = a + b\n" +
            "\n" +
            "# Printing the result\n" +
            "print(result)"
        );

        pythonSampleCodes.put(
            2,
            "# Using a for loop to print numbers from 1 through 10\n" +
            "for number in range(1, 11):  # range(1, 11) generates numbers from 1 to 10\n" +
            "    print(number)"
        );

        pythonSampleCodes.put(
            3,
            "# Variable to represent the phase of the moon\n" +
            "# True means the moon is shining (full moon), False means the moon is dark (new moon)\n" +
            "light = True  # You can change this to False to see what happens\n" +
            "\n" +
            "# Using if-else statements to choose the correct path\n" +
            "if light:\n" +
            "    print(\"The moon is shining. Choose the path to the right.\")\n" +
            "else:\n" +
            "    print(\"It's dark. Choose the path to the left.\")"
        );

        pythonSampleCodes.put(
            4,
            "def controlWaterFlow(rate):\n" +
            "    # Checking the flow rate and returning a message based on the condition\n" +
            "    if rate > 50:\n" +
            "        return \"The flow is too strong!\"\n" +
            "    elif rate < 10:\n" +
            "        return \"The flow is too weak\"\n" +
            "    else:\n" +
            "        return \"The flow is perfect!\"\n" +
            "\n" +
            "# Testing the function with different flow rates\n" +
            "print(controlWaterFlow(55))  # Output: The flow is too strong!\n" +
            "print(controlWaterFlow(5))   # Output: The flow is too weak\n" +
            "print(controlWaterFlow(25))  # Output: The flow is perfect!"
        );

        pythonSampleCodes.put(
            5,
            "def find_artifact(artifacts, target_artifact):\n" +
            "    # Searching for the target artifact in the array\n" +
            "    for index, artifact in enumerate(artifacts):\n" +
            "        if artifact == target_artifact:\n" +
            "            return f\"Artifact {target_artifact} found at position {index}\"\n" +
            "    return f\"Artifact {target_artifact} not found in the array\"\n" +
            "\n" +
            "# Array of artifact numbers\n" +
            "artifacts = [101, 203, 304, 405, 506]  # You can modify this array as needed\n" +
            "\n" +
            "# Testing the function with a target artifact number\n" +
            "print(find_artifact(artifacts, 304))  # Output: Artifact 304 found at position 2\n" +
            "print(find_artifact(artifacts, 999))  # Output: Artifact 999 not found in the array"
        );

        pythonSampleCodes.put(
            6,
            "class Hero:\n" +
            "    def __init__(self, name, strength, intelligence):\n" +
            "        self.name = name\n" +
            "        self.strength = strength\n" +
            "        self.intelligence = intelligence\n" +
            "\n" +
            "    def display_hero(self):\n" +
            "        print(f\"Hero Name: {self.name}\")\n" +
            "        print(f\"Strength: {self.strength}\")\n" +
            "        print(f\"Intelligence: {self.intelligence}\")\n" +
            "\n" +
            "# Example instantiation of a hero\n" +
            "example_hero = Hero(\"Example Hero\", 5, 3)\n" +
            "example_hero.display_hero()\n" +
            "\n" +
            "# Instantiating a hero with the name \"Marcus\", strength equal to 10, and intelligence equal to 1\n" +
            "marcus = Hero(\"Marcus\", 10, 1)\n" +
            "marcus.display_hero()"
        );

        pythonSampleCodes.put(
            7,
            "class Hero:\n" +
            "    def __init__(self, name, skill, intelligence):\n" +
            "        self.name = name\n" +
            "        self.skill = skill\n" +
            "        self.intelligence = intelligence\n" +
            "\n" +
            "    def display_hero(self):\n" +
            "        print(f\"Hero Name: {self.name}\")\n" +
            "        print(f\"Skill: {self.skill}\")\n" +
            "        print(f\"Intelligence: {self.intelligence}\")\n" +
            "\n" +
            "# Instantiating a hero\n" +
            "hero = Hero(\"Wrong Name\", 5, 7)\n" +
            "\n" +
            "# 1. Change the name of the hero to Andrew\n" +
            "hero.name = \"Andrew\"\n" +
            "\n" +
            "# 2. Increase his skill by 10\n" +
            "hero.skill += 10\n" +
            "\n" +
            "# Assuming n is already declared somewhere in your code\n" +
            "n = 10  # For example\n" +
            "\n" +
            "# 3. Modify the for loop to count from 1 to n (inclusive)\n" +
            "for i in range(1, n + 1):\n" +
            "    print(i)\n" +
            "\n" +
            "# 4. Fix the if statement to check if skill is higher than intelligence\n" +
            "if hero.skill > hero.intelligence:\n" +
            "    print(f\"{hero.name} has greater skill than intelligence.\")\n" +
            "else:\n" +
            "    print(f\"{hero.name}'s intelligence is greater than or equal to their skill.\")\n" +
            "\n" +
            "# Displaying the hero details\n" +
            "hero.display_hero()"
        );

        pythonSampleCodes.put(
            8,
            "def factorial(n):\n" +
            "    if n == 0:\n" +
            "        return 1\n" +
            "    else:\n" +
            "        return n * factorial(n - 1)\n" +
            "\n" +
            "# Calculate and print the factorial of 5\n" +
            "result = factorial(5)\n" +
            "print(\"Factorial of 5:\", result)"
        );

        pythonSampleCodes.put(
            9, // Assuming the next available key is 9
            "import random\n" +
            "\n" +
            "# Define a custom hero class\n" +
            "class Hero:\n" +
            "    def __init__(self, name, health, strength):\n" +
            "        self.name = name\n" +
            "        self.health = health\n" +
            "        self.strength = strength\n" +
            "\n" +
            "\n" +
            "# Create a list of monsters\n" +
            "monsters = [\n" +
            "    {'name': 'Goblin', 'health': 30, 'strength': 5},\n" +
            "    {'name': 'Dragon', 'health': 100, 'strength': 20},\n" +
            "    {'name': 'Troll', 'health': 50, 'strength': 10}\n" +
            "]\n" +
            "\n" +
            "# Create a hero instance\n" +
            "hero = Hero('Sir Lancelot', 100, 15)\n" +
            "\n" +
            "# Function to battle a random monster\n" +
            "def battle_monster():\n" +
            "    monster = random.choice(monsters)\n" +
            "    \n" +
            "    while hero.health > 0 and monster['health'] > 0:\n" +
            "        hero_attack = random.randint(1, hero.strength)\n" +
            "        monster_attack = random.randint(1, monster['strength'])\n" +
            "\n" +
            "        monster['health'] -= hero_attack\n" +
            "\n" +
            "        if monster['health'] <= 0:\n" +
            "            break\n" +
            "\n" +
            "        hero.health -= monster_attack\n" +
            "\n" +
            "        if hero.health <= 0:\n" +
            "            break\n" +
            "    print('Your hero defeated the monster!')\n" +
            "\n" +
            "# Main game loop\n" +
            "battle_monster()\n" +
            "  \n" +
            "print('Game over!')"
        );
    }

    public static Map<Integer, String> getSampleCodesForLanguage(String programmingLanguage) {
        switch (programmingLanguage.toLowerCase()) {
            case "java":
                return javaSampleCodes;
            case "python":
                return pythonSampleCodes;
            default:
                return new HashMap<>(); // or handle this case as needed
        }
    }
}
