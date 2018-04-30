package service;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;

public class FileHandler {
    private static final String GAMEFILE_FOLDER = "./savedGames/";
    private static final String GAMEFILE_SUFFIX = ".reversi";

    private LinkedList<HistoryStatus> loadedHistoryStatus;

    LinkedList<HistoryStatus> getLoadedHistoryStatus(){
        return loadedHistoryStatus;
    }

    private byte ownerToByte(Owner owner){
        switch (owner){
            case NONE: return 0x00;
            case BLACK: return 0x01;
            case WHITE: return 0x02;
        }

        return 0x7F;
    }

    private Owner byteToOwner(byte v){
        switch (v){
            case 0x01: return Owner.BLACK;
            case 0x02: return Owner.WHITE;
        }
        return Owner.NONE;
    }

    boolean saveToFile(String filename, LinkedList<HistoryStatus> statusLinkedList){
        checkDir();

        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
                    GAMEFILE_FOLDER.concat(filename).concat(GAMEFILE_SUFFIX)
            )));

            for (HistoryStatus status: statusLinkedList){
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        out.writeByte(ownerToByte(status.get(i , j)));

                out.writeByte(ownerToByte(status.getPlayer()));
            }
            out.close();
        }
        catch (FileNotFoundException e){
            return false;
        }
        catch (IOException e){
            return false;
        }
        return true;
    }

    String[] getAvailableFileList(){
        File testFolder = new File(GAMEFILE_FOLDER);
        return testFolder.list((dir,name) -> {
            String[] nSplit = name.split("\\.");
            String suffix = nSplit[nSplit.length - 1];
            return Objects.equals("." + suffix, GAMEFILE_SUFFIX);
        });
    }

    boolean loadFromFile(String filename){
        loadedHistoryStatus = new LinkedList<>();
        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(
                    new FileInputStream(GAMEFILE_FOLDER.concat(filename))));
            while (true){
                byte[] loadBytes = new byte[65];
                int loadResult = in.read(loadBytes);

                if (loadResult == -1)
                    break;
                HistoryStatus currentLoadingStatus = new HistoryStatus();
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++)
                        currentLoadingStatus.set(i,j ,byteToOwner(loadBytes[i * 8 + j]));
                currentLoadingStatus.setPlayer(byteToOwner(loadBytes[64]));
                loadedHistoryStatus.add(currentLoadingStatus);
            }

        }
        catch (FileNotFoundException e){
            return false;
        }
        catch (IOException e){
            return false;
        }

        return true;
    }

    // Check whether the directory exists

    private void checkDir(){
        File dirTest = new File(GAMEFILE_FOLDER);
        if(!dirTest.exists())
            dirTest.mkdir();
    }
}