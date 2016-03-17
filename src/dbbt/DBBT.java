package dbbt;
import java.util.Scanner;
import java.util.Formatter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.lang.NumberFormatException;


public class DBBT {
    //Attribues
    public ArrayList<Buyer> buyers;
    public ArrayList<DinosaurBone> Bones;
    public char[][] map;
    public ArrayList<MapO> MapNode;
    public boolean files_loaded;
    public boolean map_loaded;
    
    public Scanner keyboard;
    public Formatter screen;
            
  
            
    
    //constructor
    public DBBT() {
        Bones = new ArrayList<>();
        map = new char[60][20];
        buyers = new ArrayList<>();
        MapNode = new ArrayList<>();
        
        keyboard = new Scanner(System.in);
        screen = new Formatter(System.out);
        
        files_loaded = false;
        map_loaded = false;
    }
    
    ////////DBBT functions///////////////
    public int main_menu() {
        //display main menu
        this.screen.format("Dinosaur Bone Buying Tool\n");
        this.screen.format("1. Load the World Map.\n");
        this.screen.format("2. Show the World Map with Dinosaur bones.\n");
        this.screen.format("3. Buy Dinosaur Bone\n");
        this.screen.format("4. Save files.\n");
        this.screen.format("5. Load files.\n");
        this.screen.format("6. Exit\n");
        this.screen.format(":> ");
        
        //get user input and return to main
        int input ;
            try{
                String input_string = this.keyboard.nextLine().trim();
                input = Integer.parseInt(input_string);

            }
            catch(NumberFormatException ex){
                input = 0;
            }
            
        return input;
    }
    
    public void navigate_to_func(int input) {
        //get user input and call functions accordingly
        switch(input) {
            case 1:
                this.LoadMap();
                break;
            case 2:
                //showbonecoordinates();
                this.show_map();
                break;
            case 3:
                this.buy_bone();
                break;
            case 4:
                this.saveFiles();
                break;
            case 5:
                this.load_files();
                break;
        }
    
    }
     public void showbonecoordinates(){
            DinosaurBone temp = null;
            int i;
            for(i=0; i !=Bones.size();i++){
                
                temp = Bones.get(i);
                
                    System.out.printf("%d:%d|||lat%f:long%f-->%s\n", temp.coordinates.x, temp.coordinates.y,temp.coordinates.latit, temp.coordinates.longi, temp.name);
            }
        
        }
    //FILE SAVING FUNCTION
    public void saveFiles() {
        //call file save functions
        boolean buyer_file = this.saveBuyerFile();
        boolean bone_file = this.saveBoneFile();
        
        //give output to user about file saving
        if(buyer_file || bone_file) {
            this.screen.format("\nFiles Saved!\n\n");
        }
        else{
            this.screen.format("\nNo data to save to files..\n\n");
        }
    }
    
    public boolean saveBuyerFile() {
        File BuyerFile;
        Formatter OutputFile;
        boolean saved = false;
        
        //check if there are buyers to save
        if(!this.buyers.isEmpty()) {
            try {
                BuyerFile = new File("Buyers.txt");
                OutputFile = new Formatter(BuyerFile);

                //get buyer info and put buyers into file
                for(int i=0; i<this.buyers.size(); ++i) {

                    //get variables
                    int id = this.buyers.get(i).ID;
                    String name = this.buyers.get(i).name;
                    String email = this.buyers.get(i).email;

                    //put variables into file line by line
                    OutputFile.format("%d,%s,%s%n", id, name, email);
                }   
                
                //set saved boolean to true
                saved = true;
                //close file
                OutputFile.close();

            } catch (FileNotFoundException ex) {
                //ERROR LOADING FILE
            }        

        }
        
        return saved;
    }
    
    public boolean saveBoneFile() {
    
        File BoneFile;
        Formatter OutputFile;
        boolean saved = false;
        
        //check if there are buyers to save
        if(!this.Bones.isEmpty()) {
            try {
                BoneFile = new File("Bones.txt");
                OutputFile = new Formatter(BoneFile);
                

                //get bone info and put bones into file
                for(int i=0; i<this.Bones.size(); ++i) {
                    
                    //temp values for bone attributes
                    int id = this.Bones.get(i).boneID;
                    int age = this.Bones.get(i).age;
                    float price = this.Bones.get(i).price;
                    float weight = this.Bones.get(i).weight;
                    int bought = this.Bones.get(i).bought;
                    float globe_longitude = this.Bones.get(i).coordinates.longi;
                    float globe_latitude = this.Bones.get(i).coordinates.latit;
                    float length = this.Bones.get(i).dimensions.length;
                    float width = this.Bones.get(i).dimensions.width;
                    float height = this.Bones.get(i).dimensions.height;
                    
                    int buyer_id = 0;
                    //check if bones has buyer, then set to bone's buyer_id 
                    if(bought == 1) {
                        if(this.Bones.get(i).buyer != null) {
                            buyer_id = this.Bones.get(i).buyer.ID;
                        }
                    }
                    
                    String name = this.Bones.get(i).name;
                    String condition = this.Bones.get(i).condition;
                    String country = this.Bones.get(i).country;
                    String prospector = this.Bones.get(i).prospector;
                    
                    //put data into file line by line
                    OutputFile.format("%d,%d,%f,%f,%d,%f,%f,%f,%f,%f,%d,%s,%s,%s,%s%n", 
                        id, age, price, weight, bought, globe_longitude, 
                        globe_latitude, length, width, height, buyer_id, name, 
                        condition, country, prospector);
                }   

                //saved boolean set to true 
                saved = true;
                //close file
                OutputFile.close();

            } catch (FileNotFoundException ex) {
                //ERROR LOADING FILE
            }        

        }
        
        return saved;
    }
    
    //Map Loading Function
    public void LoadMap() {
        File MapFile;
        Scanner ScanFile;
        String Buffer;
        Scanner ScanBuffer;
        
        //clear MapNode arraylist if not empty
        if(!this.MapNode.isEmpty()) {
            this.MapNode.clear();
        } 
        
        try {
            MapFile = new File("Map.txt");
            ScanFile = new Scanner(MapFile);
            MapO TempMap;
            while(ScanFile.hasNextLine()) {
                Buffer = ScanFile.nextLine();
                
                //check if line is empty
                if(Buffer.trim().isEmpty()) {
                    break;
                }
                
                ScanBuffer = new Scanner(Buffer).useDelimiter(",");
                
                //getting input
                int x = ScanBuffer.nextInt();
                int y = ScanBuffer.nextInt();
                int z = ScanBuffer.nextInt();
                
                //making TempMap and adding to arrayList
                TempMap = new MapO(x,y,z);
                this.MapNode.add(TempMap);
                ScanBuffer.close();
            }
            
            this.map_loaded = true;
            //close file
            ScanFile.close();
            
            //show user output for loading map
            this.screen.format("\nMap Loaded!\n\n");
            
            
        } catch (FileNotFoundException ex) {
            //ERROR LOADING FILE
        }
        
    }
    
    //Calls file loading functions    
    public void load_files() {
        this.load_buyerFile();
        this.load_boneFile();
        this.files_loaded = true;
        this.screen.format("\nFiles Loaded!\n\n");
    }
    
    //LOADING BUYER
    public void load_buyerFile() {
        File BuyerFile;
        Scanner ScanFile;
        String Buffer;
        
        //clear buyers arraylist if not empty
        if(!this.buyers.isEmpty()) {
            this.buyers.clear();
        }
        
        
        try {
            BuyerFile = new File("Buyers.txt");
            ScanFile = new Scanner(BuyerFile);
            Buyer TempBuyer;
            
            while(ScanFile.hasNextLine()) {
                Buffer = ScanFile.nextLine();
                
                //check if line is empty
                if(Buffer.trim().isEmpty()) {
                    break;
                }
                
                //split string into words
                String[] splitted = Buffer.split(",");
                
                //parse input
                int id = Integer.parseInt(splitted[0].trim()); 
                String name = splitted[1].trim(); 
                String email = splitted[2].trim();
                
                //make tempBuyer and add to arrayList
                TempBuyer = new Buyer(name, email, new Address(), id);
                this.buyers.add(TempBuyer);
            }
            
            ScanFile.close();
        } catch (FileNotFoundException ex) {
            //ERROR LOADING FILE
        }
    }
    
    public void load_boneFile() {
        File BoneFile;
        Scanner ScanFile;
        String Buffer;
        
        //clear bones arraylist if not empty
        if(!this.Bones.isEmpty()) {
            this.Bones.clear();
        }
        
        try {
            BoneFile = new File("Bones.txt");
            ScanFile = new Scanner(BoneFile);
            DinosaurBone TempBone;
            Coordinates tempCoordinates = null;
            Dimension tempDimensions;
            
            while(ScanFile.hasNextLine()) {
                Buffer = ScanFile.nextLine();
                
                //check if line is empty
                if(Buffer.trim().isEmpty()) {
                    break;
                }
                
                //split buffer into string array 
                String[] data = Buffer.split(",");
                
                //Loading in file data from string array to variables
                int bone_id = Integer.parseInt(data[0].trim());
                int bone_age = Integer.parseInt(data[1].trim());
                float bone_price = Float.parseFloat(data[2].trim());
                float bone_weight = Float.parseFloat(data[3].trim());
                int bone_bought = Integer.parseInt(data[4].trim());
                
                //for Coordinate class
                float longitude_ = Float.parseFloat(data[5].trim());
                float latitude_ = Float.parseFloat(data[6].trim());
               
                //for Dimension class
                float length_ = Float.parseFloat(data[7].trim());
                float width_ = Float.parseFloat(data[8].trim());
                float height_ = Float.parseFloat(data[9].trim());
                
                int bone_buyer_id = Integer.parseInt(data[10].trim());
                String bone_name = data[11].trim();
                String bone_condition = data[12].trim();
                String bone_country = data[13].trim();
                String bone_prospector = data[14].trim();
                
                //make coordinate and dimension instances
                
                tempDimensions = new Dimension(length_, width_, height_);
                
                //make TempBone
                TempBone = new DinosaurBone(bone_id, bone_age, bone_price, 
                        bone_weight, bone_bought, bone_name, bone_condition,
                        bone_country, bone_prospector, tempDimensions, longitude_, latitude_); 
                

                //look for buyer
                int buyer_index = 0;
                boolean buyer_found = false;
                if(bone_bought != 0) {
                    for(int i=0; i<this.buyers.size(); ++i) {
                        if(bone_buyer_id == this.buyers.get(i).ID) {
                            buyer_index = i;
                            buyer_found = true;
                        }
                    }
                }
                 
                //if buyer found, set/point Bone.buyer to that buyer
                if(buyer_found) {
                    TempBone.buyer = this.buyers.get(buyer_index);
                }
                
                this.Bones.add(TempBone);
            }
            
            ScanFile.close();
            
        } catch (FileNotFoundException ex) {
            //ERROR LOADING FILE
        }
    
    
    }
    
    public void show_map() {
        if(!this.map_loaded) {
            //if map not loaded, give error message
            this.screen.format("\nPlease load map first..\n\n");
        }else {
            //load map from arraylist to 2D char array
            for(int i=0; i<this.MapNode.size(); ++i) {
                int x = this.MapNode.get(i).MapX;
                int y = this.MapNode.get(i).MapY;
                this.map[x][y] = get_map_symbol(this.MapNode.get(i).MapZ);
            }
            
            //if files loaded, load bones into map
            if(this.files_loaded) {
                //load bones into map
                for(int i=0; i<this.Bones.size(); ++i) {
                    //get map info from bone arraylist
                    int bought = this.Bones.get(i).bought;
                   
                    int x = this.Bones.get(i).coordinates.y;
                    int y = this.Bones.get(i).coordinates.x;
                    
                    //set symbol for bone in the map
                    this.map[x][y] = this.get_bone_map_symbol(bought);
                }
                
            }
            else{
                //give error message for bone files not loaded
                this.screen.format("\nNOTE: This map does not show bones!\n");
                this.screen.format("NOTE: Please load files to see bones!\n\n");
            }
            
            //Display Map
            this.screen.format("\n");
            for(int i = 0; i<20; ++i) {
                for(int j = 0; j<60; ++j) {
                    this.screen.format("%c", this.map[j][i]);
                }
                this.screen.format("\n");
            }
            this.screen.format("\nPress Enter to return to main menu\n");
            this.keyboard.nextLine();
        
        }
    
    }
    
    public char get_map_symbol(int input) {
        char symbol = '.';
        switch(input) {
            case 0: 
                symbol = '.';
                break;
            case 1:
                symbol = '*';
                break;
        }
    
        return symbol;
    }
    
    public char get_bone_map_symbol(int input) {
        char symbol = 'X';
        switch(input) {
            case 0: 
                symbol = 'X';
                break;
            case 1:
                symbol = '$';
                break;
        }
    
        return symbol;
    }
    
    //BUYING BONE
    public void buy_bone() {
        int bone_index = 0;
        
        if(files_loaded) {
            //can show bones
            if(this.Bones.isEmpty()) {
                this.screen.format("\nNo bones available to be sold.. Sorry!\n\n");
            }else {
                //check if any bones are still unsold
                boolean available = false;
                for(int i=0; i<this.Bones.size(); ++i) {
                    if(this.Bones.get(i).bought == 0) {
                        available = true;
                        break;
                    }
                }
                
                //Show available bones
                if(available) {
                    this.screen.format("\nBones Available for purchase: \n\n");
                    for(int i=0; i<this.Bones.size(); ++i) {
                        if(this.Bones.get(i).bought == 0) {
                            this.screen.format("Type: %s\n", this.Bones.get(i).name);
                            this.screen.format("ID: %d\n", this.Bones.get(i).boneID);
                            this.screen.format("Age: %d Years\n", this.Bones.get(i).age);
                            this.screen.format("Price: $ %.0f\n", this.Bones.get(i).price);
                            this.screen.format("Condition: %s\n\n", this.Bones.get(i).condition);
                            
                        }
                    } 

                    //Bone buying
                    while(true) {
                        this.screen.format("Enter Bone ID to buy bone\n");
                        this.screen.format("Enter '0' to return to main menu\n");
                        this.screen.format("> ");
           
                        //get user input
                        int bone_id_to_buy;
                        try{
                            String input_string = this.keyboard.nextLine().trim();
                            bone_id_to_buy = Integer.parseInt(input_string);

                        }
                        catch(NumberFormatException ex){
                            this.screen.format("\nIncorrect input.. Please try again..\n\n");
                            continue;
                        }

                        if(bone_id_to_buy == 0) {
                            //quit back to main menu
                            this.screen.format("\n");
                            break;
                        }else {
                            //check if id is correct
                            boolean found = false;
                            for(int i=0; i<this.Bones.size(); ++i) {
                                if(this.Bones.get(i).boneID == bone_id_to_buy) {
                                    found = true;
                                    bone_index = i;
                                }
                            }

                            //if bone id is correct, go to buying menu
                            if(found) {
                                this.buy_submenu(bone_index);
                                break;
                            }else{
                                this.screen.format("\nBone not found, please try again.\n\n");
                            }

                        }
                    }                    
                    
                }else {
                    this.screen.format("\nSorry.. No bones available for purchase.\n\n");
                
                }
                

            }
            
        }else {
            this.screen.format("\nNo bones to show. Please load files.\n\n");
        }
    }
    
    public void buy_submenu(int bone_index) {
        while(true) {
            int user_input;
            int buyer_index = 0;
            this.screen.format("\n1. Create New Buyer Account\n");
            this.screen.format("2. Login with ID\n");
            this.screen.format("3. Cancel\n");
            this.screen.format("> ");

            
            //get user input
            try{
                String input_string = this.keyboard.nextLine().trim();
                user_input = Integer.parseInt(input_string);
            }
            catch(NumberFormatException ex){
                this.screen.format("\nIncorrect input.. Please try again..\n");
                continue;
            }            
            
            //user_input = this.keyboard.nextInt();
            //this.keyboard.nextLine();

            if(user_input == 1) {
                //create new buyer
                this.buy_bone_new_buyer(bone_index);
                break;
            }else if(user_input == 2) {
                //load buyer
                int search_buyer_id;
                    this.screen.format("\nEnter Buyer ID\n");
                    this.screen.format("> ");

                    //get user input;
                    try{
                        String input_string = this.keyboard.nextLine().trim();
                        search_buyer_id = Integer.parseInt(input_string);
                        
                    }
                    catch(NumberFormatException ex){
                        this.screen.format("\nBuyer ID NOT FOUND!\n");
                        continue;
                    }     

                
                
                boolean buyer_found = false;
                for(int i=0; i<this.buyers.size(); ++i) {
                    if(search_buyer_id == this.buyers.get(i).ID) {
                        buyer_found = true;
                        buyer_index = i;
                        break;
                    }
                }

                if(buyer_found == false) {
                    this.screen.format("\nBuyer ID NOT FOUND!\n");
                }else{
                    //buyer is found
                    String confirm;
                    this.screen.format("\nPurchase confirmation\n");
                    this.screen.format("Type Y or Yes to confirm purchase\n");
                    confirm = this.keyboard.nextLine().trim().toLowerCase();
                    
                    if(confirm.equals("y") || confirm.equals("yes")) {
                        //purchase confirmed
                        this.Bones.get(bone_index).bought = 1;
                        this.Bones.get(bone_index).buyer = this.buyers.get(buyer_index); 
                        this.screen.format("\nPurchase confirmed.\n");
                        this.screen.format("Congratulation on your new Dinosaur Bone!\n\n");
                        break;
                        
                    }else {
                        this.screen.format("\nPurchase Cancelled..\n\n");
                        break;
                    }
                }

            }else if(user_input == 3) {
                //cancel
                break;
            }else {
                this.screen.format("\nIncorrect input.. Please try again..\n");
            }    
        }
    
    }
    
    public void buy_bone_new_buyer(int bone_index) {
        String name;
        String email;
        int new_id = 0;
        
        //get user info
        this.screen.format("\n******Creating New Buyer Account*******\n");        
        this.screen.format("Please enter the following information: \n");
        this.screen.format("Name: ");
        name = this.keyboard.nextLine();
        this.screen.format("E-mail Address: ");
        email = this.keyboard.nextLine();
        
        //Generate Buyer ID
        boolean id_is_new = true;
        while(true) {
            Random random_num = new Random();
            new_id = random_num.nextInt(10000 - 1 + 1) + 1;
            
            //check if id already exists
            for(int i=0; i<this.buyers.size(); ++i) {
                if(this.buyers.get(i).ID == new_id) {
                    id_is_new = false;
                    break; //from for-loop
                }
            }        
            if(id_is_new) {
                break;
            }
        }
        
        //user created output
        this.screen.format("\nNew Buyer Created!\n");
        this.screen.format("Your new Login ID is %d!\n", new_id);
        this.screen.format("***************************************\n\n");
        
        //confirm purchase
        String confirm;
        this.screen.format("Purchase confirmation\n");
        this.screen.format("Type Y or Yes to confirm purchase\n");
        this.screen.format("> ");
        confirm = this.keyboard.nextLine().trim().toLowerCase();

        if(confirm.equals("y") || confirm.equals("yes")) {
            //purchase confirmed
            this.buyers.add(new Buyer(name, email, new Address(), new_id));
            this.Bones.get(bone_index).buyer = this.buyers.get(this.buyers.size() - 1);
            this.Bones.get(bone_index).bought = 1;
            this.screen.format("\nPurchase confirmed.\n");
            this.screen.format("Congratulation on your new Dinosaur Bone!\n\n");

        }else {
            this.screen.format("\nPurchase Cancelled..\n\n");
        }
        
    }

    
    public static void main(String[] args) {
        
        DBBT main_tool = new DBBT();
        int menu_input;
        while(true) {
            //call main menu and get user input
            menu_input = main_tool.main_menu();
            
            //quit program if user specifes
            if(menu_input == 6) {
                main_tool.screen.format("Thank you. Good Bye.\n");
                break;
            }
            else if(menu_input > 0 && menu_input < 6) {
                //correct input
                main_tool.navigate_to_func(menu_input);
            }
            else{
                main_tool.screen.format("\nIncorrect input. Please try again.\n\n");
            }
        }
        

    }
    

}

/////////////////////////////////////////////////////////
////////////////////OTHER CLASSES///////////////////////
////////////////////////////////////////////////////////

class MapO {
    public int MapX;
    public int MapY;
    public int MapZ;
    
    public MapO(int x, int y, int z) {
        this.MapX = x;
        this.MapY = y;
        this.MapZ = z;
    }
    
}



class Buyer {
    public String name;
    public String email;
    public Address Address_;
    public int ID;
    
    public Buyer(String name_, String email_, Address Address_given, int ID_) {
        ID = ID_;
        name = name_;
        email = email_;
        Address_ = Address_given;
    }
    
}



class Address {
    public int streetNumber;
    public String streetName;
    public String city;
    public String state_province;
    public String Country;
    public int postalCode;

    public Address() 
    {
        this.streetNumber = 221;
        this.streetName = "Baker Street";
        this.city = "London";
        this.state_province = "Kazakhstan";
        this.Country = "England";
        this.postalCode = 69159;
    }
    
}



class DinosaurBone {
    public int boneID; 
    public int age; 
    public float price; 
    public String name; 
    public String condition; 
    public Coordinates coordinates; 
    public String country; //   
    public Dimension dimensions; 
    public float weight; //In Kilograms
    public String prospector;
    public Buyer buyer; 
    int bought; 
    float horizontal;
    float vertical;
            

    public DinosaurBone(int bone_id, int age_, float price_, float weight_, 
                        int bought_, String name_, String condition_,
                        String country_, String prospector_,
                        Dimension dimensions_, float y , float x) 
    {
        this.boneID = bone_id; 
        this.age = age_; 
        this.price = price_; 
        this.name = name_; 
        this.condition = condition_;
        horizontal = x;
        vertical = y;
        coordinates = new Coordinates();
        coordinates.latit = horizontal;
        coordinates.longi = vertical;
        coordinates.updatecoordinates();
        this.country = country_;
        this.dimensions = dimensions_;
        this.weight = weight_;
        this.prospector = prospector_;
        this.bought = bought_;
               
    }
    
}



class Dimension { //Bone dimensions
    //All in Metres
    public float length; 
    public float width;
    public float height;

    public Dimension(float length_, float width_, float height_) {
        this.length = length_;
        this.width = width_;
        this.height = height_;
    }
}






    