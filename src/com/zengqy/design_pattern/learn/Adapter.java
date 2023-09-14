package com.zengqy.design_pattern.learn;

/**
 * @包名: com.zengqy.design_pattern.learn
 * @author: zengqy
 * @DATE: 2023/8/28 14:59
 * @描述: 适配器模式 , 就是兼容以前的类的实现，通过传值进来就好了
 *       新的接口 类/对象适配器模式，这里使用对象适配器模式，因为类适配器模式需要继承旧的类，不好
 */
public class Adapter {
    public static void main(String[] args) {

        OldAudioPlay oldAudioPlay = new OldAudioPlayImpl();
        AudioPlayerAdapter adapter = new AudioPlayerAdapter(oldAudioPlay);
        adapter.play("mp3","/dsad");
        adapter.play("mp4","/dsad");

    }
}


interface OldAudioPlay {
    void playAudio(String file);
}

class OldAudioPlayImpl implements OldAudioPlay {

    @Override
    public void playAudio(String file) {
        System.out.println("旧的播放方式：" + file);
    }
}


interface NewAudioPlay {
    void play(String type, String file);
}

class NewAudioPlayImpl implements NewAudioPlay {

    @Override
    public void play(String type, String file) {
        System.out.println("新的播放方式 " + type + " " + file);
    }
}

class AudioPlayerAdapter implements NewAudioPlay {

    private OldAudioPlay mOldAudioPlay;

    public AudioPlayerAdapter(OldAudioPlay oldAudioPlay) {
        mOldAudioPlay = oldAudioPlay;
    }


    @Override
    public void play(String type, String file) {
        if ("mp3".equals(type)) {
            mOldAudioPlay.playAudio(file);
        } else {
            new NewAudioPlayImpl().play(type, file);
        }
    }
}

