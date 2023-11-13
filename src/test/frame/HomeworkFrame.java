package test.frame;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import test.dto.MemberDto;

public class HomeworkFrame extends JFrame {
    private final String defaultPath = "my_members.dat";
    private List<MemberDto> memberList;
    private JTable table;
    private DefaultTableModel model;

    void initTable() {
        table = new JTable();
        Vector<String> tableColumns = new Vector<>();
        tableColumns.add("num");
        tableColumns.add("name");
        tableColumns.add("addr");

        model = new DefaultTableModel(tableColumns, 0);
        table.setModel(model);
    }

    void loadMemberList() {
        memberList = MemberDto.loadDtos(defaultPath);

        model.setRowCount(0);
        memberList.stream().forEach(dto -> model.addRow(dto.toRowVec()));
        table.setModel(model);

        var columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setMaxWidth(60);
    }

    void addMemberList(MemberDto dto) {
        memberList.add(dto);
        saveMemberList();
    }

    void saveMemberList() {
        MemberDto.saveDtos(memberList, defaultPath);
        loadMemberList();
    }

    public HomeworkFrame(String title) {
        super(title);
        setBounds(100, 100, 500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initTable();
        loadMemberList();
        JPanel panel = new JPanel();
        {
            JTextField numField = new JTextField(5);
            {
                numField.setText("번호");
                numField.addFocusListener(new PlaceHolderFocusListener(numField, "번호"));
            }
            JTextField nameField = new JTextField(5);
            {
                nameField.setText("이름");
                nameField.addFocusListener(new PlaceHolderFocusListener(nameField, "이름"));
            }
            JTextField addrField = new JTextField(5);
            {
                addrField.setText("주소");
                addrField.addFocusListener(new PlaceHolderFocusListener(addrField, "주소"));
            }

            JButton addButton = new JButton("멤버 추가");
            {
                addButton.addActionListener(e -> {
                    try {
                        var num = Integer.parseInt(numField.getText());
                        var name = nameField.getText();
                        var addr = addrField.getText();
                        addMemberList(new MemberDto(num, name, addr));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                });
            }
            JButton editButton = new JButton("멤버 수정");
            {
                editButton.addActionListener(e -> {
                    try {
                        var num = Integer.parseInt(numField.getText());
                        var name = nameField.getText();
                        var addr = addrField.getText();
                        var selections = table.getSelectedRows();
                        memberList = IntStream.range(0, memberList.size())
                                .mapToObj(i -> {
                                    if (Arrays.stream(selections).anyMatch(x -> x == i)) {
                                        memberList.get(i).setNum(num);
                                        memberList.get(i).setName(name);
                                        memberList.get(i).setAddr(addr);
                                    }
                                    return memberList.get(i);
                                })
                                .collect(Collectors.toList());
                        saveMemberList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                });
            }
            JButton deleteButton = new JButton("멤버 삭제");
            {
                deleteButton.addActionListener(e -> {
                    try {
                        var selections = table.getSelectedRows();
                        memberList = IntStream.range(0, memberList.size())
                                .filter(i -> Arrays.stream(selections).allMatch(x -> x != i))
                                .mapToObj(i -> memberList.get(i))
                                .collect(Collectors.toList());
                        saveMemberList();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                });
            }

            panel.setLayout(new FlowLayout());
            panel.add(numField);
            panel.add(nameField);
            panel.add(addrField);
            panel.add(addButton);
            panel.add(editButton);
            panel.add(deleteButton);
        }

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
