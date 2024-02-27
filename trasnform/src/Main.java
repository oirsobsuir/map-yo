import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // чтение файлов
        String dir = "";
        String f1 = "УДО.json";
        String f2 = "УОСО.json";
        String logFileName = "logfile.log";
        String dataFileName = "data.json";

        if ( args != null && args.length > 0) {
            if (args[0] != null && args[0].length() > 0 ) {
                f1 = args[0];
            }
            if ( args.length > 1 && args[1] != null && args[1].length() > 0 ) {
                f2 = args[1];
            }
            if (args.length > 2 && args[2] != null && args[2].length() > 0) {
                dir = args[2];
            }
            if (args.length > 3 && args[3] != null && args[3].length() > 0) {
                dataFileName = args[3];
            }
        }

        File logFile;
        FileWriter logWriter = null;

        try {
            logFile = new File(dir+logFileName);
            //create the file.
            /*if (logFile.createNewFile()) {
                System.out.println("LogFile is created!");
            } else {
                System.out.println("LogFile already exists.");
                logFile.delete();
                logFile.createNewFile();
                System.out.println("LogFile is recreated!");
            }*/
            //write content
            logWriter = new FileWriter(logFile);
            writeToLog(logWriter,"LogFile is created!");

        } catch (IOException ex) {
            System.out.println("Ошибка создания лог-файла "+dir+logFileName);
            ex.printStackTrace();
        }

        String str1 = "";
        try {
            str1 = readFile(dir+f1);
        } catch (IOException ex) {
            //System.out.println("Ошибка чтения файла 1 "+dir+f1);
            writeToLog(logWriter, "Ошибка чтения файла 1 "+dir+f1);
            ex.printStackTrace();
        }
        String str2 = "";
        try {
            str2 = readFile(dir + f2);
        } catch (IOException ex) {
            //System.out.println("Ошибка чтения файла 2 "+dir+f2);
            writeToLog(logWriter, "Ошибка чтения файла 2 "+dir+f2);
            ex.printStackTrace();
        }
        if ( !"".equals(str1) && !"".equals(str2) ) {
            str1 = replaceStr(str1);
            str2 = replaceStr(str2);

            // парсинг данных из файлов в json
            Gson g = new Gson();
            ArrayList<Record> list1 = null;
            ArrayList<Record> list2 = null;
            Type listType = new TypeToken<ArrayList<Record>>() {
            }.getType();
            try {
                if (str1.indexOf("\"Column2\":\"район\",") > 0) {
                    // вырезаем первый блок данных
                    str1 = str1.substring(0, str1.indexOf("{")) + str1.substring(str1.indexOf("},") + 2, str1.length());
                }
                //list = g.fromJson(new String(str1.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8")), listType);
                list1 = g.fromJson(str1, listType);
            } catch (Exception ex) {
                //System.out.println("Ошибка парсинга данных из файла 1 " + f1);
                writeToLog(logWriter, "Ошибка парсинга данных из файла 1 " + f1);
                ex.printStackTrace();
            }
            try {
                //list = g.fromJson(new String(str1.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8")), listType);
                list2 = g.fromJson(str2, listType);
            } catch (Exception ex) {
                //System.out.println("Ошибка парсинга данных из файла 2 " + f2);
                writeToLog(logWriter, "Ошибка парсинга данных из файла 2 " + f2);
                ex.printStackTrace();
            }

            if ( list1 != null && list2 != null ) {

                // преобразование файлов
                String[] arrayOblName = new String[]{"Брестская", "Витебская", "Гомельская", "Гродненская", "Могилевская", "Минская", "г. Минск"};
                String[][] arrayRNName = new String[][]{
                        {"Барановичский", "Березовский", "Брестский", "Ганцевичский", "Дрогичинский", "Жабинковский", "Ивановский", "Ивацевичский", "Каменецкий", "Кобринский", "Лунинецкий", "Ляховичский", "Малоритский", "Пинский", "Пружанский", "Столинский"},
                        {"Бешенковичский", "Браславский", "Верхнедвинский", "Витебский", "Глубокский", "Городокский", "Докшицкий", "Дубровенский", "Лепельский", "Лиозненский", "Миорский", "Оршанский", "Полоцкий", "Поставский", "Россонский", "Сенненский", "Толочинский", "Ушачский", "Чашникский", "Шарковщинский", "Шумилинский"},
                        {"Брагинский", "Буда-Кошелевский", "Ветковский", "Гомельский", "Добрушский", "Ельский", "Житковичский", "Жлобинский", "Калинковичский", "Кормянский", "Лельчицкий", "Лоевский", "Мозырский", "Наровлянский", "Октябрьский (Гомельская область)", "Петриковский", "Речицкий", "Рогачевский", "Светлогорский", "Хойникский", "Чечерский"},
                        {"Берестовицкий", "Волковысский", "Вороновский", "Гродненский", "Дятловский", "Зельвенский", "Ивьевский", "Кореличский", "Лидский", "Мостовский", "Новогрудский", "Островецкий", "Ошмянский", "Свислочский", "Слонимский", "Сморгонский", "Щучинский"},
                        {"Белыничский", "Бобруйский", "Быховский", "Глусский", "Горецкий", "Дрибинский", "Кировский", "Климовичский", "Кличевский", "Костюковичский", "Краснопольский", "Кричевский", "Круглянский", "Могилевский", "Мстиславский", "Осиповичский", "Славгородский", "Хотимский", "Чаусский", "Чериковский", "Шкловский"},
                        {"Березинский", "Борисовский", "Вилейский", "Воложинский", "Дзержинский", "Клецкий", "Копыльский", "Крупский", "Логойский", "Любанский", "Минский", "Молодечненский", "Мядельский", "Несвижский", "Пуховичский", "Слуцкий", "Смолевичский", "Солигорский", "Стародорожский", "Столбцовский", "Узденский", "Червенский"},
                        {"г. Минск, Центральный", "г. Минск, Советский", "г. Минск, Первомайский", "г. Минск, Партизанский", "г. Минск, Заводской", "г. Минск, Ленинский", "г. Минск, Октябрьский", "г. Минск, Московский", "г. Минск, Фрунзенский"}
                };
                ArrayList<Obls> outList = new ArrayList();
                for (int i = 0; i < arrayOblName.length; i++) {
                    Obls obl = new Obls();
                    obl.id = String.valueOf(i + 1);
                    obl.name = arrayOblName[i];
                    String temp = arrayOblName[i].substring(0, arrayOblName[i].length() - 2);
                    String temp2 = "ИТОГО по " + temp + "ой области";
                    if (i == arrayOblName.length - 1) {
                        temp2 = "ИТОГО по " + arrayOblName[i] + "у";
                    }
                    Record r1 = getRecord(list1, temp2, null);
                    Record r2 = getRecord(list2, temp2, null);
                    if (r2 != null) {
                        obl.n1 = String.valueOf(r2.n1);
                        obl.n2 = String.valueOf(r2.n3);
                        obl.n3 = String.valueOf(r2.n4);
                    } else {
                        //System.out.println("Отсутствуют данные " + temp2 + " в файле 2 " + f2);
                        writeToLog(logWriter, "Отсутствуют данные  " + temp2 + " в файле 2 " + f2);
                    }
                    if (r1 != null) {
                        obl.n4 = String.valueOf(r1.n1);
                        obl.n5 = String.valueOf(r1.n3);
                        obl.n6 = String.valueOf(r1.n4);
                    } else {
                        //System.out.println("Отсутствуют данные " + temp2 + " в файле 1 " + f1);
                        writeToLog(logWriter, "Отсутствуют данные  " + temp2 + " в файле 1 " + f1);
                    }
            /*ArrayList<Record> recordsList1 = getRecords(list1, arrayOblName[i]);
            ArrayList<Record> recordsList2 = getRecords(list2, arrayOblName[i]);
            int size = 0; int num = 0;
            if ( recordsList1.size() >= recordsList2.size() ) {
                size = recordsList1.size();
                num = 1;
            } else {
                size = recordsList2.size();
                num = 2;
            }*/
                    ArrayList<Cities> newRecords = new ArrayList<Cities>();
                    for (int j = 0; j < arrayRNName[i].length; j++) {
                        Record tempR1 = getRecord(list1, arrayOblName[i], arrayRNName[i][j]);
                        Record tempR2 = getRecord(list2, arrayOblName[i], arrayRNName[i][j]);
                        Cities cit = new Cities();
                        cit.id = (i + 1) + "-" + (j + 1);
                        if (i < arrayOblName.length - 1) {
                            String tempStr = arrayRNName[i][j].trim();
                            if (tempStr.indexOf(" ") > 0) {
                                cit.name = tempStr.substring(0, tempStr.indexOf(" "));
                            } else {
                                cit.name = tempStr;
                            }
                        } else {
                            String tempStr = arrayRNName[i][j].trim();
                            if (tempStr.lastIndexOf(" ") > 0) {
                                cit.name = tempStr.substring(tempStr.lastIndexOf(" ") + 1, tempStr.length());
                            } else {
                                cit.name = tempStr;
                            }
                        }
                        // cit.name = arrayRNName[i][j];
                /*if ( num == 1 ) {
                    tempR1 = recordsList1.get(j);
                    tempR2 = getRecord(list2, tempR1.name, tempR1.rn);
                    cit.name = tempR1.rn;
                } else {
                    tempR2 = recordsList2.get(j);
                    tempR1 = getRecord(list1, tempR2.name, tempR2.rn);
                    cit.name = tempR2.rn;
                }*/
                        if (tempR2 != null) {
                            cit.n1 = String.valueOf(tempR2.n1);
                            cit.n2 = String.valueOf(tempR2.n3);
                            cit.n3 = String.valueOf(tempR2.n4);
                        } else {
                            //System.out.println("Отсутствуют данные по району " + arrayOblName[i] + " " + arrayRNName[i][j] + " в файле 2 " + f2);
                            writeToLog(logWriter, "Отсутствуют данные по району " + arrayOblName[i] + " " + arrayRNName[i][j] + " в файле 2 " + f2);
                        }
                        if (tempR1 != null) {
                            cit.n4 = String.valueOf(tempR1.n1);
                            cit.n5 = String.valueOf(tempR1.n3);
                            cit.n6 = String.valueOf(tempR1.n4);
                        } else {
                            //System.out.println("Отсутствуют данные по району " + arrayOblName[i] + " " + arrayRNName[i][j] + " в файле 1 " + f1);
                            writeToLog(logWriter, "Отсутствуют данные по району " + arrayOblName[i] + " " + arrayRNName[i][j] + " в файле 1 " + f1);
                        }
                        newRecords.add(cit);
                    }
                    obl.cities = newRecords;
                    outList.add(obl);
                }
                Record r1 = getRecord(list1, "ИТОГО", null);
                Record r2 = getRecord(list2, "ИТОГО", null);
                Cities cit = new Cities();
                cit.id = "r1";
                cit.name = "Республика Беларусь";
                if (r2 != null) {
                    cit.n1 = String.valueOf(r2.n1);
                    cit.n2 = String.valueOf(r2.n3);
                    cit.n3 = String.valueOf(r2.n4);
                } else {
                    //System.out.println("Отсутствуют данные ИТОГО по Республике Беларусь в файле 2 " + f2);
                    writeToLog(logWriter, "Отсутствуют данные ИТОГО по Республике Беларусь в файле 2 " + f2);
                }
                if (r1 != null) {
                    cit.n4 = String.valueOf(r1.n1);
                    cit.n5 = String.valueOf(r1.n3);
                    cit.n6 = String.valueOf(r1.n4);
                } else {
                    //System.out.println("Отсутствуют данные ИТОГО (по Республике Беларусь) в файле 1 " + f1);
                    writeToLog(logWriter, "Отсутствуют данные ИТОГО (по Республике Беларусь) в файле 1 " + f1);
                }
                Republic republic = new Republic();
                republic.data = cit;
                republic.obls = outList;

                //System.out.println("Hello world! = "+g.toJson(republic, new TypeToken<Republic>(){}.getType()));
                // запись преобразованных данных в файл
                try {
                    File file = new File(dir + dataFileName);
                    //create the file.
                    /*if (file.createNewFile()) {
                        //System.out.println("DataFile is created!");
                        writeToLog(logWriter, "DataFile is created! " + dir + dataFileName);
                    } else {
                        //System.out.println("DataFile already exists.");
                        writeToLog(logWriter, "DataFile already exists.");
                        file.delete();
                        file.createNewFile();
                        //System.out.println("DataFile is recreated!");
                        writeToLog(logWriter, "DataFile is recreated! " + dir + dataFileName);
                    }*/
                    //write content
                    FileWriter dataWriter = new FileWriter(file);
                    writeToLog(logWriter, "DataFile is created! " + dir + dataFileName);
                    dataWriter.write(g.toJson(republic, new TypeToken<Republic>() {
                    }.getType()));
                    dataWriter.close();
                } catch (IOException ex) {
                    //System.out.println("Ошибка записи данных в файл " + dir + dataFileName);
                    writeToLog(logWriter, "Ошибка записи данных в файл " + dir + dataFileName);
                    ex.printStackTrace();
                }
            }
        }

        try {
            logWriter.close();
        } catch (IOException ex) {
            System.out.println("Проблеммы при закрытии лог-файла "+dir+logFileName);
            ex.printStackTrace();
        }

    }

    /*{
     "область": "Брестская",
     "район": "Барановичский",
     "общее кол-во учреждений дошкольного образования": 11,
     "город\/поселок городского типа": 1,
     "сельская местность ": 10,
     "малокомплектные  учреждения дошкольного образования с количеством воспитанников до 10  (10 включительно)": 0
    },*/
    static class Record {
        @SerializedName(value="УДО", alternate={"область"})
        public String  name;
        @SerializedName(value="Column2", alternate={"район"})
        public String  rn;
        @SerializedName(value="Column3", alternate={"общее кол-во учреждений", "общее кол-во учреждений дошкольного образования"})
        public int  n1;
        @SerializedName(value="Column4", alternate={"город\\/поселок городского типа"})
        public int  n2;
        @SerializedName(value="Column5", alternate={"сельская местность"})
        public int  n3;
        @SerializedName(value="Column6", alternate={"учреждения в которых учится до 25 учащихся (включая 25)", "малокомплектные  учреждения дошкольного образования с количеством воспитанников до 10  (10 включительно)"} )
        public int  n4;
    }

    static class Republic {
        public Cities data;
        public ArrayList<Obls> obls;
    }

    // name: '��������� ������� ', n1: '440 ', n2: '190 ', n3: '2 ', n4: '5 ', n5: '1 ', n6: '6, 14 ���� � � ', n7: ' ', n8: ' ', cities: [
    static class Obls {
        public String id;
        public String name;
        public String n1;
        public String n2;
        public String n3;
        public String n4;
        public String n5;
        public String n6;
        public ArrayList<Cities> cities;

        public Obls() {
            id = "";
            name = "";
            n1 = "";
            n2 = "";
            n3 = "";
            n4 = "";
            n5 = "";
            n6 = "";
            cities = new ArrayList<Cities>();
        }
    }

    //{ name: 'Барановичский ', n1: '11 ', n2: '10 ', n3: ' ', n4: ' ', n5: ' ', n6: ' ', n7: ' ' , n8: ' '},
    static class Cities {
        public String id;
        public String name;
        public String n1;
        public String n2;
        public String n3;
        public String n4;
        public String n5;
        public String n6;
    }

    // возвращает информацию об одном районе
    public static Record getRecord (ArrayList<Record> list, String obl, String rn) {
        obl = obl.trim();
        if ( rn == null ) {
            for (int i=0; i<list.size(); i++) {
                if (list.get(i) != null) {
                    String name = list.get(i).name;
                    String rnTemp = list.get(i).rn;
                    if (name != null && rnTemp == null) {
                        if (obl.equalsIgnoreCase(name.trim())) {
                            return list.get(i);
                        }
                    }
                }
            }
        } else {
            rn = rn.trim();
            for (int i=0; i<list.size(); i++) {
                if (list.get(i) != null) {
                    String name = list.get(i).name;
                    String rnTemp = list.get(i).rn;
                    if (name != null && rnTemp != null) {
                        if (obl.equalsIgnoreCase(name.trim()) && rn.equalsIgnoreCase(rnTemp.trim())) {
                            return list.get(i);
                        }
                    }
                }
            }
        }
        return null;
    }

    // возвращает информацию обо всех райлнах области
    /*public static ArrayList<Record> getRecords (ArrayList<Record> list, String obl) {
        ArrayList<Record> recordsList = new ArrayList<Record>();
        for (int i=0; i<list.size(); i++) {
            if (list.get(i) != null) {
                if (list.get(i).name != null) {
                    if (list.get(i).name.equalsIgnoreCase(obl)) {
                        recordsList.add(list.get(i));
                    }
                }
            }
        }
        return recordsList;
    }*/

    // чтение файла
    public static String  readFile (String fileName) throws IOException {
        String str = "";
        //try {
            File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        Charset utf8 = Charset.forName("UTF-8");
        InputStreamReader isr = new InputStreamReader(fis, utf8);
        BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                str = str + line;
            }
            br.close();

        /*} catch (IOException ex) {
            System.out.println("Ошибка чтения файла "+fileName);
        }*/
        return str;
    }

    public static void writeToLog (FileWriter writer, String str) {
        try {
            System.out.println(str);
            if ( writer != null ) {
                writer.write(str + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Проблеммы при записи в лог-файл");
        }
    }

    public static String replaceStr (String str) {
        if ( str != null ) {
            str = str.replace("\" ", "\"");
            str = str.replace(" \"", "\"");
            str = str.replace("( ", "(");
            str = str.replace(" )", ")");
        }
        return str;
    }
}