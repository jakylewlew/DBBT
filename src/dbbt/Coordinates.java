package dbbt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open th6mplate in the editor.
 */


/**
 *
 * @author Jacob
 */
public class Coordinates {
    
    float latit;
    float longi;
    int x;
    int y;
    
    public Coordinates(){
      
      
    }
     public void updatecoordinates(){//a function for updating the xy coordinates when moved or created
        
        x = (int)(this.latit);
        y = (int)(this.longi);
        
         if( y >= 0) {
            if( y == 0) {
                y = y + 30;
            }
            else {
                y = y / 6;
                y = y + 29 ;
            }
            if(y>59){//if large positive make highest place possible
                    y = 59;
             }
         }
        else {
            
            y = y/6 + 30;
         }
         if(y<0){
             y = 0; //if large neg make zero
         }
        if(x >= 0) {
            if(x == 0) {
                x = x + 10;
            }else {
                x = x / 10;
                x = 9 - x;
            }
            if(x>19){
                x = 19;//large positive make highest pos posible
            }
        }else {
            x = x * -1;
            x = x/10;
            x = x + 10;
            
        }
        if(x<0){
            x = 0;
        }
        
    }    
         
     }


        
