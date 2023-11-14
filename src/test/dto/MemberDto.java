package test.dto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import test.safecast.ListCast;

public class MemberDto implements Serializable {
    int num;
    String name;
    String addr;

    public MemberDto() {
    }

    public MemberDto(int num, String name, String addr) {
        this.num = num;
        this.name = name;
        this.addr = addr;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Vector<Object> toRowVec() {
        Vector<Object> vec = new Vector<>();
        vec.add(num);
        vec.add(name);
        vec.add(addr);
        return vec;
    }

    public static List<MemberDto> loadMemberDtoList(String path) {
        try (var fis = new FileInputStream(path)) {
            try (var ois = new java.io.ObjectInputStream(fis)) {
                return ListCast.cast(ois.readObject(), MemberDto.class);
            }
        } catch (IOException ioe) {
            if (ioe instanceof FileNotFoundException) {
                var memberList = new ArrayList<MemberDto>();
                saveMemberDtoList(memberList, path);
                return loadMemberDtoList(path);
            }
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, cnfe.getMessage());
        }
        return null;
    }

    public static void saveMemberDtoList(List<MemberDto> members, String path) {
        try (var fos = new java.io.FileOutputStream(path)) {
            try (var oos = new java.io.ObjectOutputStream(fos)) {
                oos.writeObject(members);
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }
    }
}
