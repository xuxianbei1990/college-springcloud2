package college.springcloud.transport.controller;

/**
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 17:07
 * Version:V1.0
 */


import college.springcloud.transport.model.StudentVo;
import jdk.internal.org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.springframework.asm.Opcodes.ASM5;


public class AddField extends ClassVisitor {

    private String name;
    private int access;
    private String desc;
    private Object value;

    private boolean duplicate;

    // 构造器
    public AddField(ClassVisitor cv, String name, int access, String desc, Object value) {
        super(ASM5, cv);
        this.name = name;
        this.access = access;
        this.desc = desc;
        this.value = value;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (this.name.equals(name)) {
            duplicate = true;
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!duplicate) {
            FieldVisitor fv = super.visitField(access, name, desc, null, value);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }

    /**
     * 获得类文件的 File 对象
     *
     * @return
     */
//    private File getFile() {
//        if(classFile == null) {
//            StringBuffer sb = new StringBuffer();
//            sb.append(clazz.getResource("/"))
//                    .append(clazz.getCanonicalName().replace(".", File.separator))
//                    .append(CLASS_FILE_SUFFIX);
//            classFile = new File(sb.substring(6));
//        }
//        return classFile;
//    }
    public static void main(String[] args) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(StudentVo.class.getResource("/"))
                .append(StudentVo.class.getCanonicalName().replace(".", File.separator))
                .append(".class");

        StringBuffer output = new StringBuffer();
        output.append(StudentVo.class.getResource("/"))
                .append(StudentVo.class.getCanonicalName().replace(".", File.separator))
                .append("Ex")
                .append(".class");
        ClassReader classReader = new ClassReader(new FileInputStream(sb.toString().substring(6)));
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor addField = new AddField(classWriter,
                "field",
                Opcodes.ACC_PRIVATE,
                Type.getDescriptor(String.class),
                "demo"
        );
        classReader.accept(addField, ClassReader.EXPAND_FRAMES);
        byte[] newClass = classWriter.toByteArray();

        File newFile = new File(output.toString().substring(6));
        newFile.createNewFile();
        new FileOutputStream(newFile).write(newClass);
    }
}
