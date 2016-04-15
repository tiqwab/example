package org.tiqwab.example.gradle.multi;

import java.util.Properties;

public class BatMainImport extends BatMainImportBase {

	@Override
	public void execute() {
		Properties jdbcProp = ApplicationProp.load();
		System.out.println(jdbcProp.get("app.import.message"));
	}

	public static void main(String[] args) throws Exception {
		BatMainImport batMain = new BatMainImport();
		batMain.execute();
	}
	
}
