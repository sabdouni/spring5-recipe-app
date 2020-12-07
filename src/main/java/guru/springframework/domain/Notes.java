package guru.springframework.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne(mappedBy="notes")
    private Recipe recipe;

    @Lob
    private String recipeNotes;

    public Notes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }

    public Notes() {
    }

}
