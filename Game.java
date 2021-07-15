package last_challenge;
import java.util.*;
public class Game {
    static final int mapH = 5;//エリアの縦の広さ
    static final int mapW = 5;//エリアの横の広さ
    static boolean map[][] = new boolean[mapH][mapW];//マップ
    static Ship ships[] = new Ship[3];//船のインスタンス
    static int turncount = 1;//ターン数
    static int dropx;//爆弾を落とすX座標
    static int dropy;//爆弾を落とすY座標

    

    //スタート画面
    public static void disTitle(){
        System.out.println("***********************");
        System.out.println("       戦艦ゲーム       ");
        System.out.println("***********************");
    }

    //クリア画面
    public static void disEnd(){
        System.out.println("***********************");
        System.out.println("      ゲームクリア       ");
        System.out.println("***********************");
    }
    
    //初期設定
    public static void init(){
        
        Random rd = new Random();
        int count = 0;//船の数
        while(count < 3){
            int x = rd.nextInt(mapH);
            int y = rd.nextInt(mapW);
            //X,Yの地点に他の船がないなら
            if(!map[x][y]){
                map[x][y] = true;//マップに船を置く
                ships[count] = new Ship(count+1,x,y);//インスタンス化
                count++;
            }
        }
    }

    //船を動かす
    public static void move(Ship ship){
        Random rd = new Random();
        while(true){
            int nx = rd.nextInt(mapH);
            int ny = rd.nextInt(mapW);
            //X,Yの地点に他の船がないなら
            if(!map[nx][ny]){
                map[nx][ny] = true;//マップに船を置く
                //座標の更新
                ship.setX(nx);
                ship.setY(ny);
                break;
            }
        }
    }
    //各ターンの画面表示
    public static void disTrun(){
        System.out.printf("-----[ターン%d]-----\n",turncount);
        for(Ship ship : ships){
            //指定の船の生存確認
            ship.disViability();
        }
        //ターンを増やす
        turncount++;
    }

    //座標の入力
    public static void input(){
        Scanner sc = new Scanner(System.in);
        try{

            System.out.printf("爆弾のX座標を入力してください(1-%d)\n",mapH);
            dropx = sc.nextInt()-1;
            System.out.printf("爆弾のY座標を入力してください(1-%d)\n",mapW);
            dropy = sc.nextInt()-1;

            //入力値がマップ外の時
            if(dropx<0 || dropy<0 || dropx>=mapH || dropy>=mapW){
                System.out.println("指定された範囲内の数値を入力されてください");
                input();//もう一度入力を行う
            }

        }catch(Exception e){//数値以外が入力された時
            System.out.println("数値を入力してください");
            input();//もう一度入力を行う
        }
        sc.close();
        
    }

    //爆弾を落とす
    public static void dropbomb(){
        for(Ship ship : ships){
            if(ship.gethp()>0){//船が生きてるなら
                
                int cheak = ship.damage(dropx,dropy);//各船に対する影響
                
                if(cheak == 0){
                    System.out.printf("%s : はずれ！\n",ship.getname());
                }else if(cheak == 1){
                    System.out.printf("%s : 波高し！\n",ship.getname());
                }else if(cheak == 2){
                    System.out.printf("%s : 当たった！\n",ship.getname());

                    //今の座標をリセット
                    map[ship.getx()][ship.gety()] = false;
                    //新しい座標の設定
                    move(ship);

                }else{
                    System.out.printf("%s : 撃沈！\n",ship.getname());
                    
                    //座標のリセット
                    map[ship.getx()][ship.gety()] = false;
                }
            }else{//船が撃沈しているなら
                System.out.printf("%s : 撃沈済み\n",ship.getname());
            }
            
        }
    }

    //ゲームを続けるかどうかの判定
    public static boolean proceed(){
        int living_ship = 0;//生きてる船の数

        //生きてる船を数える
        for(Ship ship : ships){
            if(ship.gethp()>0){
                living_ship++;
            }
        }
        if(living_ship>0){
            return true;
        }else{
            return false;
        }
    }


    
}
