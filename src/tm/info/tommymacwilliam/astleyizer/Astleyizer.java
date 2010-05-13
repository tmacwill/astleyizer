package tm.info.tommymacwilliam.astleyizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

public class Astleyizer {
    private File astleyFile;

    /**
     * Constructor
     * @param astleyFile Song to overwrite other songs with
     */
    public Astleyizer(File astleyFile) {
	this.astleyFile = astleyFile;
    }

    /**
     * Constructor
     * @param astleyFile Path to song to overwrite other songs with
     */
    public Astleyizer(String astleyFile) {
	this.astleyFile = new File(astleyFile);
    }

    /**
     * Replace a song with the astleyFile, backing it up first
     * @param song Song to replace
     * @param backupPath Path to back up song to
     */
    public void astleyizeFile(File song, String backupPath) {
        try {
            // back up file first
            this.copyFile(song, new File(backupPath + File.separator + song.getName()));

            // get ID3 tags from song, then delete it
            HashMap<String, String> tags = this.readTags(song);
            song.delete();

            // get path of song to overwrite without filename
            String songPath = song.getAbsolutePath();
            songPath = songPath.substring(0, songPath.lastIndexOf(File.separator));

            // copy astleyFile, but rename to original song name
            File newFile = this.copyFile(this.astleyFile, new File(songPath + File.separator + song.getName()));
            // rewrite astleyFile ID3 data to match that of original song
            this.writeTags(newFile, tags);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy contents to readFile to writeFile, creating directories if necessary
     * @param readFile File to copy
     * @param writeFile Destination file
     * @return Destination file
     */
    private File copyFile(File readFile, File writeFile) {
        // get path of writeFile without filename
        String writeFilePath = writeFile.getAbsolutePath();
        writeFilePath = writeFilePath.substring(0, writeFilePath.lastIndexOf(File.separator));
        File writeFileDir = new File(writeFilePath);

        try {
            // create all necessary parent directories and new, blank file
            writeFileDir.mkdirs();
            writeFile.createNewFile();
            InputStream in = new FileInputStream(readFile);
            OutputStream out = new FileOutputStream(writeFile);
	    // create copy buffer
            byte[] buf = new byte[1024];
            int len;
	    // copy contents of readFile into writeFile
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch (Exception e) {
	    e.printStackTrace();
        }
        finally {
            // create new file in given directory
            return writeFile;
        }

    }

    /**
     * Read ID3 tags from song
     * @param song Song to read from
     * @return Map from ID3 tag name to value
     */
    private HashMap<String, String> readTags(File song) {
        // create new hashmap to store tags
        HashMap<String, String> tags = new HashMap<String, String>();
        
	try {
	    // create ID3 objects for song
	    AudioFile readAudioFile = AudioFileIO.read(song);
	    Tag readTag = readAudioFile.getTag();

	    // save all tag information from original song
	    tags.put("track", readTag.getFirstTitle());
	    tags.put("artist",readTag.getFirstArtist());
	    tags.put("album", readTag.getFirstAlbum());
	    tags.put("year", readTag.getFirstYear());
	    tags.put("genre", readTag.getFirstGenre());
	    tags.put("comment", readTag.getFirstComment());
	}
	catch (Exception e) {
	    e.printStackTrace();
	}

	return tags;
    }

    /**
     * Write tags to song
     * @param song Song to write tags to
     * @param tags Map of ID3 tag values
     */
    private void writeTags(File song, HashMap<String, String> tags) {
	try {
	    // create ID3 objects for song
	    AudioFile writeAudioFile = AudioFileIO.read(song);
	    Tag writeTag = writeAudioFile.getTag();

	    // change ID3 information of RickRoll
	    writeTag.setTitle(tags.get("track"));
	    writeTag.setArtist(tags.get("artist"));
	    writeTag.setAlbum(tags.get("album"));
	    writeTag.setYear(tags.get("year"));
	    writeTag.setGenre(tags.get("genre"));
	    writeTag.setComment(tags.get("comment"));

	    writeAudioFile.commit();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}