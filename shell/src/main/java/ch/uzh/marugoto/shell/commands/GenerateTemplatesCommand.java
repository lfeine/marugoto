package ch.uzh.marugoto.shell.commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import ch.uzh.marugoto.core.data.entity.Chapter;
import ch.uzh.marugoto.core.data.entity.CheckboxExercise;
import ch.uzh.marugoto.core.data.entity.DateExercise;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageTransition;
import ch.uzh.marugoto.core.data.entity.RadioButtonExercise;
import ch.uzh.marugoto.core.data.entity.Storyline;
import ch.uzh.marugoto.core.data.entity.TextComponent;
import ch.uzh.marugoto.core.data.entity.TextExercise;
import ch.uzh.marugoto.core.data.entity.Topic;
import ch.uzh.marugoto.shell.util.FileGenerator;

@ShellComponent
public class GenerateTemplatesCommand {

	private final Map<String, Object> IMPORT_INSTANCES = Map.of(
			"topic", new Topic(),
			"storyline", new Storyline(null, false),
			"chapter", new Chapter(),
			"page", new Page(),
			"textComponent", new TextComponent(0, null),
			"textExercise", new TextExercise(0, 0, 0, null),
			"radioButtonExercise", new RadioButtonExercise(0, null, null),
			"checkboxExercise", new CheckboxExercise(0, null, null, null, null, null),
			"dataExercise", new DateExercise(0, false, null, null, null),
			"pageTransition", new PageTransition(null, null, null)
	);

	@ShellMethod("Generates folder structure and empty json files")
	public void generateTemplateFiles(String destinationPath) {

		var destinationFolder = FileGenerator.generateFolder(destinationPath);

		if (destinationFolder.isFile()) {
			importFromJsonFile(destinationFolder);
		} else {
			System.out.println("PATH ERROR: Path to json file is needed. (Path to folder provided)");
		}
	}

	private void importFromJsonFile(File file) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
			var storylineKey = jsonObject.keySet().iterator().next().toString();

			if (!storylineKey.equals("storyline")) {
				System.out.println("FILE ERROR: not a valid json file");
				return;
			}

			var destinationFolder = file.getParentFile();
			// TOPIC
			FileGenerator.generateJsonFileFromObject(new Topic(), "topic", destinationFolder);
			// STORY LINES
			generateStorylineTemplates(jsonObject, storylineKey, IMPORT_INSTANCES.get(storylineKey), destinationFolder);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateStorylineTemplates(JSONObject jsonParentObject, String jsonKey, Object entity, File destination) {
		var isValid = IMPORT_INSTANCES.containsKey(jsonKey);
		if (!isValid) {
			System.out.println("FILE ERROR: not a valid json file");
		}

		JSONArray jsonList = (JSONArray) jsonParentObject.get(jsonKey);

		for (int j = 0; j < jsonList.size(); j++) {
			var generatedFolder = FileGenerator.generateFolder(destination.getPath(), jsonKey + (j + 1));
			FileGenerator.generateJsonFileFromObject(entity, jsonKey, generatedFolder);
			JSONObject jsonObject = (JSONObject) jsonList.get(j);

			if (jsonKey.equals("page")) {
				for (Object o : jsonObject.keySet()) {
					var property = (String) o;
					var val = (Long) jsonObject.get(property);
					FileGenerator.generateJsonFileFromObject(IMPORT_INSTANCES.get(property), property, generatedFolder, val.intValue());
				}
			} else {
				var nextJsonKey = jsonObject.keySet().iterator().next().toString();
				generateStorylineTemplates(jsonObject, nextJsonKey, IMPORT_INSTANCES.get(nextJsonKey), generatedFolder);
			}
		}
	}
}