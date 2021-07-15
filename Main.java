package last_challenge;

public class Main {
    public static void main(String[] args) {
        Game.disTitle();
        
        Game.init();
        
        boolean flag = true;

        while(flag){
            Game.disTrun();
            
            Game.input();
            Game.dropbomb();
            flag = Game.proceed();
        }

        Game.disEnd();
    }
}
