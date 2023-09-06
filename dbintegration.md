## DB Integration

---

* ***@Entity***
  ```java
  @Entity
  public class Blog extends BaseEntity {
      // ...
  }
  ```
* ***@Table*** - Specifies the primary table for the annotated entity.
  ```java
  @Entity
  @Table(name = "table_blogger")
  public class Blogger extends BaseEntity {
      // ...
  }
  ```
  Hibernate:
  ```SQL
  create table table_blogger (
    ...
  ```
* ***@Transient*** - Specifies that the property or field is not persistent.
  ```java
  @Transient
  private boolean loggedIn = false;
  ```
* ***@Column*** - Specifies the mapped column for a persistent property or field.
  * **name** 
    ```java
    @Entity
    @Table(name = "table_blogger")
    public class Blogger extends BaseEntity {
        @Column(name = "pass")
        private String password;
        // ...
    }
    ```
    Hibernate:
    ```SQL
    create table table_blogger (
      pass varchar(255),
      ...
    ```
  * **fetch**
    ```java
    @OneToMany(mappedBy = "blogger", cascade = CascadeType.ALL, fetch = ???)
    private List<Blog> blogs = new ArrayList<>();
    ```
    ```java
    Blogger blogger = bloggerRepository.findById(1L).orElseThrow();
    System.out.println(blogger.getBlogs());
    ```
    * FetchType.LAZY
    ```SQL
    -- Blogger blogger = bloggerRepository.findById(1L)
    select
        blogger0_.id as id1_1_0_,
        blogger0_.username as username2_1_0_ 
    from
        blogger blogger0_ 
    where
        blogger0_.id=?
    ```
    ```SQL
    -- blogger.getBlogs()
    select
        blogs0_.blg as blg3_0_0_,
        blogs0_.id as id1_0_0_,
        blogs0_.id as id1_0_1_,
        blogs0_.blg as blg3_0_1_,
        blogs0_.title as title2_0_1_ 
    from
        blog blogs0_ 
    where
        blogs0_.blg=?
    ```
    Sometimes can fail with 
    > .LazyInitializationException: could not initialize proxy [com.gfa.springsql.model.Blogger#1] - no Session
  
    *Fix 1* - (not recommended) - `spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true`

    *Fix 2* - Usage of `@Transactional` in your `@Service` layer
    ```java
    @Transactional(readOnly = true)
    public int countBlogsByBloggerId(Long id) {
        return bloggerRepository.getById(id).getBlogs().size();
    }
    ```
    * FetchType.EAGER
    ```SQL
    -- Blogger blogger = bloggerRepository.findById(1L)
    select
        blogger0_.id as id1_1_0_,
        blogger0_.username as username2_1_0_,
        blogs1_.blg as blg3_0_1_,
        blogs1_.id as id1_0_1_,
        blogs1_.id as id1_0_2_,
        blogs1_.blg as blg3_0_2_,
        blogs1_.title as title2_0_2_ 
    from
        blogger blogger0_ 
    left outer join
        blog blogs1_ 
            on blogger0_.id=blogs1_.blg 
    where
        blogger0_.id=?
    ```
* ***@MappedSuperclass*** - Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
  ```java
  @MappedSuperclass
  public class BaseEntity {
      @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  }
  ```
* ***@Id*** - Specifies the primary key of an entity.
  ```java
  @Id private Long id;
  ```
* ***@GeneratedValue*** - Provides for the specification of generation strategies for the values of primary keys.
  ```java
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  ```
* ***@Enumerated*** - Specifies that a persistent property or field should be persisted as a enumerated type.
  ```java
  // file: rating.java
  public enum Rating {
      BAD, GOOD, EXCELENT
  }
  ```
  ```java
  // file: Post.java
  @Enumerated(value = EnumType.STRING)
  private Rating rating;
  ```
* ***@Query***
  ```java
  public interface CommentRepository extends JpaRepository<Comment, Long> {
  
      @Query("select c from Comment c where text like %:substring%")
      List<Comment> findCommentContainingSting(String substring);
  }
  ```
* ***@Temporal*** - Annotation must be specified for persistent fields or properties of type `java.util.Date` and `java.util.Calendar`. It may only be specified for fields or properties of these types.
  ```java
  private Calendar calendar;

  @Temporal(TemporalType.DATE)
  private Calendar date;
  
  @Temporal(TemporalType.TIME)
  private Date time;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar timestamp;
  ```
  | calendar | date | time | timestamp
  |---|---|---|---|
  | *datetime* | *date* | *time* | *datetime*
  | 2021-09-27 15:02:10.267000 | 2021-09-27 | 15:02:10 | 2021-09-27 15:02:10.267000

### Constraints
* ***Not null (JPA)***
  ```java
  @Column(nullable = false)
  private String username;
  ```
  Hibernate:
  ```SQL
  create table table_blogger (
     ...
     username varchar(255) not null,
  ```
* ***Unique***
  ```java
  @Column(nullable = false, unique = true)
  private String username;
  ```
  Hibernate:
  ```SQL
  alter table table_blogger 
    add constraint UK_ap2t1ctdrr06r9nyd09jtglan unique (username)
  ```

### Mapping
* ***@OneToOne***
  * `table_blogger` owns the FK 
    ```java
    @Entity
    @Table(name = "table_blogger")
    @Getter @Setter
    public class Blogger extends BaseEntity {
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "contact_id")
        private ContactDetails contactDetails;
    }
    ```
    ```java
    @Entity
    @Table(name = "contact_details")
    @NoArgsConstructor
    public class ContactDetails {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String email;
        private String telNumber;
  
        @OneToOne(mappedBy = "contactDetails")
        private Blogger blogger;
    }
    ```
    ![one2one1](img/one2one1.png)

  * Using shared primary key:

    ```java
    @Entity
    @Table(name = "contact_details")
    @NoArgsConstructor
    @Getter @Setter
    public class ContactDetails {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String email;
      private String telNumber;
  
      @OneToOne
      @MapsId
      @JoinColumn(name = "blogger_id")
      private Blogger blogger;
    }
    ```
    ```java
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "blogger")
    @PrimaryKeyJoinColumn
    private ContactDetails contactDetails;
    ```
    ![one2one2](img/one2one2.png)

* ***OneToMany***
  ```java
  @OneToMany(mappedBy = "blogger", cascade = CascadeType.ALL)
  private List<Blog> blogs = new ArrayList<>();
  ```
  ```java
  @ManyToOne
  @JoinColumn(name = "blg")
  private Blogger blogger;
  ```
  ![one2many](img/one2many.png)
* ***ManyToMany***
  ```java
  @ManyToMany
  @JoinTable(name = "post_category",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();
  ```
  ```java
  @ManyToMany(mappedBy = "categories")
  private Set<Post> posts;
  ```
  ![many2many](img/many2many.png)

### MyISAM vs. InnoDB
* [What are the main differences between InnoDB and MyISAM?](https://dba.stackexchange.com/a/15)
* [Introduction to InnoDB](https://dev.mysql.com/doc/refman/5.6/en/innodb-introduction.html)
* Use InnoDB storage engine: `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57InnoDBDialect`

### More Links
* [Query Creation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
* [Learn JPA & Hibernate](https://www.baeldung.com/learn-jpa-hibernate)
* [jhipster](https://start.jhipster.tech/jdl-studio/)

## Web App Troubleshooting

---

* Debugger is your friend. Setting up breakpoints in your endpoints and inspecting code & data can save you hours.

  ![meme](img/meme.png)
* Inspecting page source can reveal improperly generated HTML & CSS (Thymeleaf)
* Use browser's Developer Tools:
  * GET & POST params
  * Redirects
* Turn on logging SQL queries
  * For Hibernate
    ```properties
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.show-sql=true
    ```
  * For MySQL
    ```SQL
    set global log_output = 'FILE';
    set global general_log_file = 'C:/tmp/mysql.log';
    set global general_log = on;
    ```

### Utils
* ER diagram using Workbench: `Database -> Reverse Engineer...`
