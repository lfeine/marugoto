package ch.uzh.marugoto.shell.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

import ch.uzh.marugoto.core.data.entity.Criteria;
import ch.uzh.marugoto.core.data.entity.Exercise;
import ch.uzh.marugoto.core.data.entity.ExerciseCriteriaType;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageCriteriaType;
import ch.uzh.marugoto.core.data.repository.ComponentRepository;
import ch.uzh.marugoto.core.data.repository.PageRepository;
import ch.uzh.marugoto.shell.util.BeanUtil;

public class CriteriaDeserializer extends StdDeserializer<Criteria> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CriteriaDeserializer() {
        this(null);
    }

    public CriteriaDeserializer(Class<?> vc) {
        super(vc);		
    }

    public Criteria deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Criteria criteria = new Criteria();

        if (node.has("affectedExercise")) {
            var affectedExercise = node.get("affectedExercise");
            var exerciseCriteria = node.get("exerciseCriteria");

            if (exerciseCriteria.isTextual()) {
                criteria.setExerciseCriteria(ExerciseCriteriaType.valueOf(exerciseCriteria.asText()));
            }

            if (affectedExercise.isObject()) {
                var exercise = (Exercise) BeanUtil.getBean(ComponentRepository.class).findById(affectedExercise.get("id").asText()).orElse(null);
                criteria.setAffectedExercise(exercise);
            }
        }
        
        if (node.has("affectedPage")) {
        	var affectedPage = node.get("affectedPage");
        	var pageCriteria = node.get("pageCriteria");

        	if (pageCriteria.isTextual()) {
                criteria.setPageCriteria(PageCriteriaType.valueOf(pageCriteria.asText()));
            }

        	if (affectedPage.isObject()) {
                var page = (Page) BeanUtil.getBean(PageRepository.class).findById(affectedPage.get("id").asText()).orElse(null);
                criteria.setAffectedPage(page);
            }
        }

        return criteria;
    }
}
