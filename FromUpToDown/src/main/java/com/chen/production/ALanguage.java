package com.chen.production;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ALanguage {
    private ArrayList<Character> NotTerminal;  //所有的非终结符
    private ArrayList<Character> Terminal; //所有的终结符
    private List<Language> list;   //终结符和非终结符的文法
    private List<Language> list1;/////替换后的文法
    private List<Language> list2;//////A->c替换后如A->cZ
    private List<Character> list3;///有左递归的非终结符
    private List<Language> list4;////保存原有文法
    public ALanguage() {
        NotTerminal = new ArrayList<Character>();
        Terminal = new ArrayList<Character>();
        list = new ArrayList<Language>();
        list1= new ArrayList<Language>();
        list2= new ArrayList<Language>();
        list3= new ArrayList<Character>();
        list4= new ArrayList<Language>();

    }

    /**
     * @param s 将用户输入的String类型文法进行转化
     */
    public void createALanguage(String s[]) throws Exception {
        //将String类型的输入转化为文法
        for (int i = 0; i < s.length; i++) {
            Language language = new Language();
            language.S2L(s[i]);  //将String类型的文法进行转化
            System.out.println("**"+language);
            list.add(language);
            System.out.println("%%"+list);
            //找到文法中所有的非终结符
            if (NotTerminal.contains(language.getNTerminal()) == false) {
                NotTerminal.add(language.getNTerminal());
            }
            //找寻文法中的所有终结符
            for (int i1 = 0; i1 < language.getMatch().length; i1++) {
                Character c = language.getMatch()[i1];
                if ((c < 'A' || c > 'Z') && Terminal.contains(c) == false) {
                    if (c == '$') {
                        continue;
                    }
                    //扫描文法中的每一个match 如果Terminal中不存在就加入Terminal中
                    Terminal.add(language.getMatch()[i1]);
                }
            }
        }
        getLeftrecursiontolist();
    }

    public List<Language> getList() {
        return list;
    }

    public ArrayList<Character> getNotTerminal() {
        return NotTerminal;
    }

    public ArrayList<Character> getTerminal() {
        return Terminal;
    }

    /**
     * 从所有文法中，找出所有非终结符nt的文法
     * @param nt 非终结符
     * */
    public List<Language> getLanguage(Character nt) {
        List<Language> ntLanguage = new ArrayList<>();
        Language language = null;
        for (int i = 0; i < list.size(); i++) {
            language = list.get(i);
            if (language.getNTerminal() == nt) {
                ntLanguage.add(language);
            }
        }
        return ntLanguage;
    }

    private void exchangMatch(char []m,char nt){
      for(int i=0;i<m.length;i++)
      {
          if(i==m.length-1)
          {
              m[i]= nt;
              break;
          }
          m[i]=m[i+1];
      }
    }
 private void getLeftrecursiontolist()////消除左递归
 {
     char[]exnt={' ','Z','Y','X','W','V','U','T','S','R','Q','P','O','N'};///替换符号
     Language language1 = null;
     int n=0;
     for(int i=0;i<list.size();i++)
     {
         if(list.get(i).getNTerminal()==list.get(i).getMatch1(0))
         {
             if(list3.size()==0||(list3.get(n-1)!=list.get(i).getNTerminal()))
             {
                 list3.add(list.get(i).getNTerminal());
                 n++;
               //System.out.println("list3"+list3);
             }
             language1=list.get(i);
             language1.setNT(exnt[n]);
             NotTerminal.add(language1.getNTerminal());

             exchangMatch(language1.getMatch(),language1.getNTerminal());
             list1.add(language1);
//             list1.get(list1.size()-1).setNT(exnt[n]);
//             System.out.println("list1"+list1.get(list1.size()-1).getNTerminal());
//             exchangMatch(list1.get((list1.size()-1)).getMatch(),list1.get((list1.size()-1)).getNTerminal());
             //System.out.println("list1###"+list1);
         }
         else if(list.get(i).getNTerminal()!=list.get(i).getMatch1(0))
         {

             if((list.get(i).getNTerminal()!=list3.get(list3.size()-1))&&(list2.size()==0))
             {
                 language1.setNT(list3.get(list3.size()-1));
                 char []match2=new char[1];
                 match2[0]=list1.get(list1.size()-1).getNTerminal();
                 //System.out.println(" match2[0]"+ match2[0]);
                 language1.setMatch1(match2);
                 list2.add(language1);
             }
             if(list.get(i).getNTerminal()==list3.get(list3.size()-1))
             {
                 language1=list.get(i);
                 //System.out.println("language1"+language1.getlength()+language1.getMatch1(0));
                    char []match1 = new char[language1.getlength()+1];
                 //System.out.println("list2.get(i).length"+list2.get(0).getlength());
                     for(int k=0;k<=language1.getlength();k++)
                     {
                         if(k==language1.getlength())
                         {
                             match1[k]=list1.get(list1.size()-1).getNTerminal();
                             break;
                         }
                         match1[k]= language1.getMatch1(k);

                     }
                     language1.setMatch1(match1);
                     list2.add(language1);
             }
             else
             {
                 language1=list.get(i);
                 list4.add(language1);
             }
         }
     }
      list.removeAll(list);
     list.addAll(0,list2);
     list.addAll(list2.size(),list1);
     list.addAll(list2.size()+list1.size(),list4);
     System.out.println("***list"+list);


 }



}
