import java.io.Serializable;

/**
 * 
 * @author Huang Shih-Hao
 * @class Student class
 * @properties id: the nth student object num: the student's ID name: the
 *             student's name, either English or Chinese sport: the favorite
 *             sport option course: the favorite course option
 */
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int count = 0;
	private int id;
	private String num;
	private String name;
	private int sport;
	private int course;

	public Student(String num, String name, int sport, int course) {
		Student.count++;
		this.id = Student.count;
		this.num = num;
		this.name = name;
		this.sport = sport;
		this.course = course;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSport(int sport) {
		this.sport = sport;
	}

	public void setCourse(int course) {
		this.course = course;
	}

	public int getId() {
		return this.id;
	}

	public String getNum() {
		return this.num;
	}

	public String getName() {
		return this.name;
	}

	public int getSport() {
		return this.sport;
	}

	public int getCourse() {
		return this.course;
	}

	/*
	 * Override the equals of an Object, the student's num and name are not
	 * allowed to be same
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Student s = (Student) obj;
		return (this.num.toLowerCase().equals(s.num.toLowerCase()) || this.name.equals(s.name));
	}

	public boolean fequals(String num, String name, int sport, int course) {
		return this.num.toLowerCase().equals(num.toLowerCase()) && this.name.equals(name) && this.sport == sport && this.course == course;
	}
}
