package NotepadPackage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Класс хранит список объектов класса Note, предоставляет интерфейс для
 * записи его в файл, чтения в него из файла, добавления, удаления и поиска в нем элементов*/
public class Notepad {
    ArrayList<Note> notes;

    public Notepad() {
        notes=new ArrayList<>();
        readFromFile();
    }

    public static void main(String []args) {
        Notepad notepad=new Notepad();
        notepad.start();
    }

    /**Начало работы с блокнотом*/
    public void start() {
        mainMenu();
    }

    /**Метод отображает главное меню приложения и реагирует на действия пользователя*/
    private void mainMenu() {
        Scanner sc=new Scanner(System.in);
        String menu="1-view notes; 2-search note; 3-add note; 0-exit";
        System.out.println(menu);
        int choice=sc.nextInt();
        while (choice!=0) {
            if (choice==1) {
                viewNotes();
            }
            if (choice==2) {
                searchBlock();
            }
            if (choice==3) {
                addBlock();
            }
            System.out.println("\n"+menu);
            choice=sc.nextInt();
        }
        if (choice==0)
            System.exit(0);
    }

    /**Код, выполняемый при выборе функции поиск книги.
     * Вынесен в отдельный метод для повышения читабельности кода
     * в методе основного меню mainMenu()*/
    private void searchBlock() {
        Note note=new Note();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter topic of note, if u know (if u don't, just skip): ");
        try {
            note.setTopic(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Enter email of note, if u know (if u don't, just skip): ");
        try {
            note.setEmail(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Enter text to search, if u remember (if u don't, just skip): ");
        try {
            note.setText(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("There are some similar notes for you:");
        System.out.println(getNotes(sortNotes(searchNote(note))));
    }

    /**Код, выполняемый при выборе функции добавления книги.
     * Вынесен в отдельный метод для повышения читабельности кода
     * в методе основного меню mainMenu()*/
    private void addBlock() {
        Note note=new Note();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter topic: ");
        try {
            note.setTopic(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalDate lc=LocalDate.now();
        LocalTime lt=LocalTime.now();
        Date date=new Date( lc.getYear()-1900, lc.getMonthValue()-1, lc.getDayOfMonth()
                , lt.getHour(), lt.getMinute(), lt.getSecond());
        note.setDate(date);


        boolean flag=false;
        do {
            System.out.println("Enter email: ");
            try {
                note.setEmail(reader.readLine());
                Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(note.getEmail());
                flag=matcher.find();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(!flag);

        System.out.println("Enter text: ");
        try {
            note.setText(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        addNote(note);
    }

    /**Поиск заметки*/
    private ArrayList<Note> searchNote(Note note) {
        ArrayList<Note> searchedNotes=new ArrayList<>();
        for (Note n: notes) {
            if (n.consistsOf(note)) {
                searchedNotes.add(n);
            }
        }
        return searchedNotes;
    }

    /**Метод добавляет заметку в список с заметками и записывает его в файл*/
    private void addNote(Note note) {
        notes.add(note);
        writeToFile();
    }

    /**Функция вывода на экран всех заметок в блокноте*/
    private void viewNotes() {
        System.out.println("All notes:\n"+ getNotes(notes));
    }

    /**Запись все заметок в списке notes в файл*/
    private void writeToFile() {
        try {
            FileOutputStream fos = new FileOutputStream("Notes.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.flush();
            oos.close();
        } catch (Exception e) {
        }
    }

    /**Чтение всех хранящихся заметок из файла*/
    private void readFromFile() {
        try {
            FileInputStream fis = new FileInputStream("Notes.txt");
            ObjectInputStream oin = new ObjectInputStream(fis);
            notes=(ArrayList<Note>) oin.readObject();
        } catch (Exception e) {
        }
        finally {
            if (notes==null) {
                notes= new ArrayList<>();
            }
        }
    }

    /**Возвращает строку с информацией о всех заметках в блокноте*/
    private String getNotes(ArrayList<Note> notes) {
        String res=new String();
        for (int i=0; i< notes.size(); i++) {
            res=res+notes.get(i)+"\n";
        }
        return res;
    }

    /**Сортировка списка с заметками по дате (от самой последней до самой старой)*/
    private ArrayList<Note> sortNotes(ArrayList<Note> notes) {
        Note []arr=new Note[notes.size()];
        notes.toArray(arr);
        ArrayList<Note> newNotes=new ArrayList<>();
        int min;
        for (int i=0; i<arr.length; i++) {
            min=i;
            for(int j=i+1; j<arr.length; j++) {
                if (arr[j].getDate().compareTo(arr[i].getDate())>0) {
                    min = j;
                }
            }
            if (min!=i){
                Note n=arr[i];
                arr[i]=arr[min];
                arr[min]=n;
            }
            newNotes.add(arr[i]);
        }
        return newNotes;
    }
}
