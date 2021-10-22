import java.util.Random;
import java.util.Scanner;

import rpgcreature.Braver;
import rpgcreature.Slime;
import rpgcreature.Wizard;
import rpgcreature.MetalSlime;
import rpgcreature.Golem;
import rpgcreature.Monster;

//////////////////////////////////////
// メインクラス
/////////////////////////////////////
public class RPGMain {
    private final int MONSTER_NUM=3;
    private final int COMMAND_BATTLE=1;
    private final int COMMAND_RECOVERY=2;
    private final int SLINE = 0;
    private final int WIZSRD = 1;
    private final int METALSLIME = 2;
    private final int GOLEM = 3;

    private Braver braver;
    private Monster[] monsters;
    private static int turn=0;
    private static boolean winflag = false;
    private static int getGold = 0;
    public static void main(String[] args){
        RPGMain rpg = new RPGMain();
        //ゲームスタート
        rpg.game();
    }

    /**
     * ゲームメインメソッド
     */
    public void game(){

        //タイトル表示
        dispTitle();

        //名前入力
        Scanner sc = new Scanner(System.in);
        System.out.println("あなたの名前を入力してください");
        String name = sc.nextLine();
        //入力された名前で主人公（勇者をインスタンス作成）
        braver = new Braver(name);

        //バトルスタートを表示する
        dispBattleStart();

        //敵を3体ランダムに決める
        dicideMonsters();

        //メインループ（無限ループ）
        loop:
        while(true){
            //現在の状態を表示
            dispTrun();
            boolean flag = true;
            //入力されたコマンドを取得
            do{
                dispStatus();
                int command = sc.nextInt();
                
                if( command == COMMAND_BATTLE ){
                    //たたかう
                    flag = false;
                    if( !battle() ){
                        break loop;
                    }
                }else if( command == COMMAND_RECOVERY ){
                    //回復する
                    flag = false;
                    braver.recovery();
                }else if( command == GOLEM ){
                    System.out.println("===1又は2を入力してください===");
                }
            }while(flag);
        }
        if(winflag){
            dispWin();
        }
        sc.close();

        

        
    }

    /**
     * タイトルを表示する
     */
    private void dispTitle(){
        System.out.println("==========================");
        System.out.println("=       ASO QUEST        =");
        System.out.println("==========================");
    }

    /**
     * バトルスタートの表示
     */
    private void dispBattleStart(){
        System.out.println("==========================");
        System.out.println("====BATTLE START!!!!!!====");
        System.out.println("==========================");
    }

    /**
     * 現在の状態を表示する
     */
    private void dispStatus(){
        System.out.println("==========================");
        System.out.printf( "= %s                 =\n",braver.getName());
        System.out.printf( "= HP:%3d                 =\n",braver.getHp());
        System.out.println("==========================");
        System.out.println("どうしますか？1:たたかう 2:回復");
    }

    private void dispTrun(){
        turn++;
        System.out.printf("=========%dターン目========\n",turn);
        
    }

    private void dispWin(){
        System.out.printf("全てのモンスターを倒した！\n");
        System.out.printf("%dのゴールドを獲得した\n",getGold);
        
    }

    /**
     * モンスターを3体決定する
     */
    private void dicideMonsters(){
        Random r = new Random();
        monsters = new Monster[MONSTER_NUM];
        for(int i=0; i < MONSTER_NUM; i++){
            //乱数を取得してモンスターを決定する
            int value = r.nextInt(4);
            if( value == SLINE ){
                monsters[i] = new Slime();
            }else if( value == WIZSRD){
                monsters[i] = new Wizard();
            }else if( value == METALSLIME){
                monsters[i] = new MetalSlime();
            }else{
                monsters[i] = new Golem();
            }
        }
        
        //「〇〇〇が現れた」を表示
        for(int i = 0; i < MONSTER_NUM; i++){
            monsters[i].displayAppearanceMsg();
        }
    }

    /**
     * たたかうコマンドに対する処理
     * 
     *  バトル継続するかのフラグ true：継続する false：バトル終了
     */
    private boolean battle(){
        //どのモンスターに攻撃するかを決定する
        Random r = new Random();
        Monster monster = null;
        //モンスター存在確認
        do{
            int index = r.nextInt(3);
            monster = monsters[index];
        }while( !monster.isThere() );

        //主人公→モンスターへ攻撃！
        braver.attack(monster);
        if( !monster.isAlive() ){
            System.out.printf("%sを倒した！\n",monster.getName());
            getGold += monster.getGold();
        }
        
        //3体居なくなった？
        if( isNotThereAllMonster() ){
            //すべて居なくなったら終了
            winflag = true;
            return false;
        }

        //モンスター→主人公からの攻撃
        for(int i=0; i < MONSTER_NUM;i++){
            if(monsters[i].isThere()){
                monsters[i].attack(braver);
            }
            
        }

        //主人公が死んだか？
        if( !braver.isAlive() ){
            System.out.println("あなたはしにました");
            return false;
        }

        return true;
    }

    /**
     * 全てのモンスターが居なくなったか？
     * @return true:すべて居なくなった false:まだモンスターは居る
     */
    private boolean isNotThereAllMonster(){
        boolean isNotThereMonster = true;
        for(int i=0; i < MONSTER_NUM; i++){
            if( monsters[i].isThere() ){
                isNotThereMonster = false;
                break;
            }
        }
        return isNotThereMonster;
    }
}
