package Bab5.src;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Kelas ini bertanggung jawab khusus untuk memutar musik latar (background music).
 * Semua metode di dalamnya bersifat 'static' agar mudah dipanggil dari mana saja
 * tanpa perlu membuat objek baru (contoh: BackgroundMusicPlayer.play()).
 */
public class BackgroundMusicPlayer {

    // Variabel static untuk menampung klip musik
    private static Clip musicClip;
    // Penanda untuk memastikan musik hanya di-load sekali saja
    private static boolean isInitialized = false;

    /**
     * Menginisialisasi dan me-load file musik dari path yang diberikan.
     * Harus dipanggil sekali di awal program sebelum memanggil play() atau stop().
     * @param musicFilePath Path ke file musik Anda, contoh: "audio/background_music.wav"
     */
    public static void init(String musicFilePath) {
        // Mencegah proses load berulang jika sudah pernah dilakukan
        if (isInitialized) {
            return;
        }

        try {
            // Mengambil file dari folder resources/package
            URL url = BackgroundMusicPlayer.class.getClassLoader().getResource(musicFilePath);
            if (url == null) {
                System.err.println("File musik tidak ditemukan di path: " + musicFilePath);
                return;
            }

            // Mempersiapkan audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);

            // Tandai bahwa inisialisasi telah berhasil
            isInitialized = true;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error saat memuat file musik latar:");
            e.printStackTrace();
        }
    }

    /**
     * Memainkan musik latar secara terus-menerus (looping).
     */
    public static void play() {
        // Cek apakah klip sudah siap dan volume tidak di-mute
        if (musicClip != null && SoundEffect.volume != SoundEffect.Volume.MUTE) {
            // Hanya mulai mainkan jika musik tidak sedang berjalan
            if (!musicClip.isRunning()) {
                musicClip.setFramePosition(0); // Selalu mulai dari awal
                musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Mainkan secara berulang
            }
        }
    }

    /**
     * Menghentikan musik latar yang sedang berjalan.
     */
    public static void stop() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }
}