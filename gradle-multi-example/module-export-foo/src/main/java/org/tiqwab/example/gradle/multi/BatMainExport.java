package org.tiqwab.example.gradle.multi;

import java.util.Properties;

public class BatMainExport extends BatMainExportBase {

	@Override
	public void execute() {
		Properties jdbcProp = ApplicationProp.load();
		System.out.println(jdbcProp.get("app.export.message"));
	}

	public static void main(String[] args) throws Exception {
		BatMainExport batMain = new BatMainExport();
		batMain.execute();
	}

}
