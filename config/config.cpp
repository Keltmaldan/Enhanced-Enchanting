/*
    this file isn't used in the mod
    it was intended to be in config/enhanced_enchanting but it was refused by CF
    you can still download it to modify your config folder
*/
#include <iostream>
#include <fstream>
#include <string>

const std::string author = "Keltmaldan";
const std::string license = "WTFPL (for this file only)";

int main() {
    menu :
    std::cout << "Welcome to the config folder of Enhanced Enchanting & Enchantments by " << author << ".\n";
    std::cout << "What do you want to modify ?\n";
    std::cout << "0 : Enchantment weight\n";
    std::cout << "1 : Enchantment max level\n";
    std::cout << "2 : Remove an enchantment\n";
    std::cout << "3 : Anvil cost\n";
    std::cout << "4 : Exit\n";
    int choice;
    std::string fileName;
    bool number = false;
    std::cin >> choice;
    switch (choice) {
        case 0 :
            fileName = "weight.txt";
            number = true;
            break;
        case 1 :
            fileName = "max_level.txt";
            number = true;
            break;
        case 2 :
            fileName = "remove.txt";
            number = false;
            break;
        case 3 :
            fileName = "anvil_cost.txt";
            number = true;
            break;
        case 4 :
            return 0;
        default :
            std::cout << "Error invalid number\n";
            goto menu;
    };
    second_choice :
    std::cout << "What do you want to do ?\n";
    std::cout << "0 : Add a new entry\n";
    std::cout << "1 : Remove an entry\n";
    std::cout << "2 : Add all the enchantment\n";
    std::cout << "3 : Clear the config parameter\n";
    std::cout << "4 : Return to the main menu\n";
    std::cin >> choice;
    bool all = false;
    bool remove = false;
    switch (choice) {
        case 0 :
            break;
        case 1 :
            remove = true;
            break;
        case 2 :
            all = true;
            break;
        case 3 :
            all = remove = true;
            break;
        case 4 :
            goto menu;
        default :
            std::cout << "Error invalid number\n";
            goto second_choice;
    };



    std::fstream file(fileName);
    std::ofstream newVersion("temporary.txt");
    std::string enchantName = "all";
    std::string line;

    if (!all) { // ask for enchantment id
        std::cout << "Enter enchantment id\n";
        std::cin >> enchantName;
    }
    if (!remove) { // add to the file (all or not)
        while (std::getline(file, line)) {
            newVersion << line << '\n';
        }
        std::string value = "0";
        if (number) {
            std::cout << "Enter new value\n";
            std::cin >> value;
            newVersion << enchantName << '$' << value << '\n';
            goto end;
        }
        newVersion << enchantName << '\n';
        goto end;
    }
    //adding a "else" has no interest since the last scope go to menu once finished
    if (all) { //remove all
        goto end;
    }
    { //remove a single one
        unsigned char i = 0;
        std::ofstream newVersion("temporary.txt");
        while (std::getline(file, line)) {
            if (line.find(enchantName) == std::string::npos) {
                newVersion << line << '\n';
            }
        }
        goto end;
    }
    end :
        auto name = fileName.c_str();
        file.close();
        newVersion.close();
        std::remove(name);
        std::rename("temporary.txt", name);
        goto menu;
    // should never be reached
    return 0;
}
