package com.kaya.ixdreader.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class downReader implements Reader {

    private String path2Tag;
    private FileOpenUtils fileUtils;
    private String lastContent="";
    private int mReadMode = 0;
    private String[] flag;
    private List<String> mReaderContent = new ArrayList<>();
    public downReader(String path){
        path2Tag = path;
        fileUtils = new FileOpenUtils(path);
        mReadMode = ListDataSaveUtil.getInstance().GetReadMode();
        SetReadMode(mReadMode);
        RecoveryLastOrInitContent();
    }
    private void SetReadMode(int mode){
        if(mode == ListDataSaveUtil.READ_MODE){

            flag = new String[]{"。", "？", ".", "'"};
        }
        else if(mode == ListDataSaveUtil.TALK_MODE){
            flag = new String[]{"“", "”", "\"", "'"};
        }
        else {
            flag = new String[]{"“", "”", "\"", "'"};
        }
    }

    private void RecoveryLastOrInitContent(){
        SaveContentHistory saveContentHistory = ListDataSaveUtil.getInstance().toGetContentHistory(path2Tag);

        if(saveContentHistory == null){
            mReaderContent.add("001");
            mReaderContent.add("002");
            mReaderContent.add("003");
            fileUtils.openBook(0);
        }
        else {
            for(String content :saveContentHistory.getSaveContent()){
                mReaderContent.add(content);
            }
            fileUtils.openBook(saveContentHistory.getEndCurPos());
            this.lastContent = saveContentHistory.getLastContent();
        }
    }
    @Override
    public List<String> getContentList() {
        // TODO Auto-generated method stub
        if(mReaderContent.size() > 2) {
            mReaderContent.remove(0);
        }
        if(!fileUtils.hasNextParagraph()){
            mReaderContent.add("已经是最后一页了");
            return mReaderContent;
        }
        mReaderContent.add(ManageLastContent());
        return mReaderContent;
    }

    public void saveReadHistory() throws Exception {
        SaveContentHistory saveContentHistory = new SaveContentHistory();
        saveContentHistory.setEndCurPos(fileUtils.getCurEndPos());
        saveContentHistory.setLastContent(lastContent);
        saveContentHistory.setSaveContent(mReaderContent);
        ListDataSaveUtil.getInstance().toSaveContentHistory(path2Tag,saveContentHistory);
    }

    public int getReadHistory(){
        return 1;
    }

    /**此处的逻辑，是对于获取到的段落进行处理，如果不存在，那么，就需要进行进行下一次读取了，如果存在，那么，就从这里面获取，并且截断 */
    private String ManageLastContent(){
        int index;
        StringBuilder temp = new StringBuilder();
        String tempe;
        if((index = isContentValid(lastContent)) == -1){
            temp.append(lastContent);
            temp.append(readNewParagraph());
            return temp.toString();
        }
        tempe = lastContent.substring(0,index);
        lastContent = lastContent.substring(index+1);
        return tempe;
    }

    private StringBuilder readNewParagraph(){
        String paragraph = fileUtils.readNextParagraph();
        StringBuilder mBuffer = new StringBuilder();
        int index;
        while((index = isContentValid(paragraph)) == -1){

            if(!fileUtils.hasNextParagraph()){
                return mBuffer;
            }
            mBuffer.append(paragraph);
            paragraph =  fileUtils.readNextParagraph();
        }
        mBuffer.append(paragraph.subSequence(0, index));
        lastContent = paragraph.substring(index+1);
       // System.out.println(lastContent+"000009");
        return mBuffer;
    }
    private int isContentValid(String inputParagraph){
        int index = -1;
        for(int i=0;i<flag.length;i++){
            if(inputParagraph.indexOf(flag[i])==-1){
                //
                continue;
            }
            if (index == -1){
                index = inputParagraph.indexOf(flag[i]);
            }
            else if(index > inputParagraph.indexOf(flag[i])){
                index = inputParagraph.indexOf(flag[i]);
            }
        }
        return index;
    }

}
    