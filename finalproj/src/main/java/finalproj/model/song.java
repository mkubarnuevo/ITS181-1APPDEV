package finalproj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "songs")
public class song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String videoPath;

    @Column(nullable = false)
    private String lyricsPath;

    public song() {}

    public song(String title, String videoPath, String lyricsPath) {
        this.title = title;
        this.videoPath = videoPath;
        this.lyricsPath = lyricsPath;
    }

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getVideoPath() { 
        return videoPath; 
    }

    public void setVideoPath(String videoPath) { 
        this.videoPath = videoPath; 
    }

    public String getLyricsPath() { 
        return lyricsPath; 
    }

    public void setLyricsPath(String lyricsPath) { 
        this.lyricsPath = lyricsPath; 
    }
}
