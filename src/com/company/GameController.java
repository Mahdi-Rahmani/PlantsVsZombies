package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;

public class GameController {
    //The y coordinates that zombies can entre from
    private int[] zombies_Y_Pos;
    //The game state
    private GameState state;
    //The step of the game
    private int step;

    /**
     * level constructor
     * @param state the state og game
     */
    public GameController(GameState state) {
        this.state = state;
        zombies_Y_Pos = new int[]{80, 175, 270, 365, 460};
        step = 1;

    }

    /**
     * in this method we create any objects that we need in the beginning of the game
     */
    public void init() {
        //adding the lawn mowers
        int[] firstLawnMowerFirstPos = new int[]{260, 80};
        int[] secondLawnMowerFirstPos = new int[]{260, 175};
        int[] thirdLawnMowerFirstPos = new int[]{260, 270};
        int[] forthLawnMowerFirstPos = new int[]{260, 365};
        int[] fifthLawnMowerFirstPos = new int[]{260, 460};

        state.addElement(new LawnMower(firstLawnMowerFirstPos, state));
        state.addElement(new LawnMower(secondLawnMowerFirstPos, state));
        state.addElement(new LawnMower(thirdLawnMowerFirstPos, state));
        state.addElement(new LawnMower(forthLawnMowerFirstPos, state));
        state.addElement(new LawnMower(fifthLawnMowerFirstPos, state));


        //adding the flowers
        int[] sunFlowerCardFirstPos = new int[]{70,10};
        int[] peaShooterCardFirstPos = new int[]{123,10};
        int[] snowPeaShooterCardFirstPos = new int[]{176,10};
        int[] mushroomCardFirstPos = new int[]{229,10};
        int[] wallNutCardsFirstPos = new int[]{282,10};
        int[] cherryBombCardsFirstPos = new int[]{335,10};

        state.addElement(new SunFlowerCard(sunFlowerCardFirstPos, state));
        state.addElement(new PeaShooterCard(peaShooterCardFirstPos, state));
        state.addElement(new SnowPeaShooterCard(snowPeaShooterCardFirstPos, state));
        state.addElement(new MushroomCard(mushroomCardFirstPos, state));
        state.addElement(new WallNutCard(wallNutCardsFirstPos, state));
        state.addElement(new CherryBombCard(cherryBombCardsFirstPos, state));

        //adding shovel
        int[] shovelFirstPos = new int[]{120,500};
        state.addElement(new Shovel(shovelFirstPos, state));

        //adding menu button
        int[] menuPos = new int[]{40,450};
        state.addElement(new GameMenuButton(menuPos,150,40,state));

    }


    /**
     * check whether a step is completed or not
     * we have five step
     * the first step is for planting and achieve suns( the duration is 5 sec)
     * the second part is first coming of zombies( the duration is 2.5 min and in each 30 sec one zombie is coming)
     * the second part is second coming of zombies( the duration is 3 min and in each 30 sec two zombie are coming)
     * the second part is third coming(Huge wave) of zombies( the duration is 2.5 min and in each 25 sec two zombie are coming)
     */
    public void update() {
        switch (step)
        {
            case 1:
            {
                step = 0;
                Timer firstStepTimer = new Timer();
                TimerTask firstStepTask = new TimerTask() {
                    @Override
                    public void run() {
                        state.setMessageAddress("");
                        // file seda handle shavad
                        step = 2;
                        firstStepTimer.cancel();
                    }
                };
                firstStepTimer.schedule(firstStepTask, 50000);
            }
            break;
            case 2:
            {
                if (state.getGameSetting().getSound() == 0) {
                    playSound();
                }
                step = 0;
                final int[] zombieNumber1 = {0};
                Timer secondStepTimer = new Timer();
                TimerTask secondStepTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (zombieNumber1[0] < 5)
                        {
                            zombieMaker();
                            zombieNumber1[0]++;
                        }
                        else {
                            state.setMessageAddress("");
                            step = 3;
                            secondStepTimer.cancel();
                        }
                    }
                };
                secondStepTimer.schedule(secondStepTask, 2000,30000);
            }
            break;
            case 3:
            {
                if (state.getGameSetting().getSound() == 0) {
                    playSound();
                }
                step = 0;
                final int[] zombieNumber2 = {0};
                Timer thirdStepTimer = new Timer();
                TimerTask thirdStepTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (zombieNumber2[0] < 11)
                        {
                            zombieMaker();
                            zombieMaker();
                            zombieNumber2[0] += 2;
                        }
                        else {
                            zombieNumber2[0] = 0;
                            state.setMessageAddress("");
                            step = 4;
                            thirdStepTimer.cancel();
                        }
                    }
                };
                thirdStepTimer.schedule(thirdStepTask, 2000,30000);
            }
            break;
            case 4:
            {
                if (state.getGameSetting().getSound() == 0) {
                    playSound();
                }
                step = 0;
                final int[] zombieNumber3 = {0};
                Timer forthStepTimer = new Timer();
                TimerTask forthStepTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (zombieNumber3[0] < 11)
                        {
                            zombieMaker();
                            zombieMaker();
                            zombieNumber3[0] += 2;
                        }
                        else {
                            zombieNumber3[0] = 0;
                            state.setMessageAddress("");
                            step = 5;
                            forthStepTimer.cancel();
                        }
                    }
                };
                forthStepTimer.schedule(forthStepTask, 2000,25000);
            }
            break;
            case 5:
            {
                if (state.getDeadZombies() >= 30){
                    //WINNER
                }
            }
        }

    }

    /**
     * this is zombie maker method
     * it creat random zombie and add it to the elements list in game state
     */
    public void zombieMaker()
    {
        Random random = new Random();
        //we dont know which row the zombie is coming inside (it is random)
        int randomY = zombies_Y_Pos[random.nextInt(5)];
        //The type of the coming zombie is also random
        int randomType = random.nextInt(3);
        //we should set the first position of zombie
        int[] zombieFirstPosition = new int[]{1050 , randomY};

        Zombie zombie = null;
        switch (randomType)
        {
            case 0:
                zombie = new NormalZombie(zombieFirstPosition, state.getGameSetting().getZombieSpeed()[0], state.getGameSetting().getZombieAttackPower()[0], state);
                break;
            case 1:
                zombie = new ConeHeadZombie(zombieFirstPosition, state.getGameSetting().getZombieSpeed()[1], state.getGameSetting().getZombieAttackPower()[1], state);
                break;
            case 2:
                zombie = new BucketHeadZombie(zombieFirstPosition, state.getGameSetting().getZombieSpeed()[2], state.getGameSetting().getZombieAttackPower()[2], state);
                break;
        }
        state.addElement(zombie);
    }

    /**
     * the zombies are coming !!!
     */
    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./PVS Design Kit/sounds/zombies_coming.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
