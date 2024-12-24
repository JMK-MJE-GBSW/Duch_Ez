    package BE.Duch_Ez.entity.group;

    import BE.Duch_Ez.entity.user.UserEntity;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

    @Getter
    @Setter
    @Entity
    @Table(name = "group_entity")
    public class GroupEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;  // UUID 타입으로 변경

        @Column(nullable = false)
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "owner_id", nullable = false)
        private UserEntity owner;

        @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ParticipantEntity> participants = new ArrayList<>();

        @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DuchPayEntity> duchPays = new ArrayList<>();

    }
