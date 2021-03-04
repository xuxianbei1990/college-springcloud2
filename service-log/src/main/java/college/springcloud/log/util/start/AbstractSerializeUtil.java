package college.springcloud.log.util.start;


import java.io.*;

public abstract class AbstractSerializeUtil implements Serializable {

	private static final long serialVersionUID = 1L;


	private final String SERIALIZE_NAME = "serialize_" + this.getClass().getSimpleName().split("\\$")[0];
	private final String FILE_PATH = System.getProperty("java.io.tmpdir") + "/serialize";


	{
		File paths = new File(FILE_PATH);
		if (!paths.exists()) {
			paths.mkdir();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> serialize()));
	}


	/**
	 * 获取序列化实体类
	 *
	 * @return
	 */
	public abstract Object getEntity();


	protected void serialize() {
		Object entity = getEntity();
		if (entity == null) {
			return;
		}

		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(FILE_PATH, SERIALIZE_NAME));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(entity);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (oos != null) {
					oos.close();
				}

			} catch (IOException e) {

			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {

			}

		}

	}

	protected Object deserializer() {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			File file = new File(FILE_PATH, SERIALIZE_NAME);
			if (!file.exists()) {
				return null;
			}
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object object = ois.readObject();

			return object;

		} catch (Exception e) {


		} finally {
			try {
				if (ois != null) {
					ois.close();
				}

			} catch (IOException e) {

			}
			try {
				if (fis != null) {
					fis.close();
				}

			} catch (IOException e) {

			}
		}
		return null;
	}
}
