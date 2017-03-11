package etiennedesticourt.makurajapanese.Skill;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SkillFileParser {
    private static final String SKILL_ROOT_TAG = "skill";
    private static final String TITLE_TAG = "title";
    private static final String LESSON_TAG = "lesson";
    private static final String WORDS_TAG = "words";
    private static final String QUESTION_TAG = "question";
    private static final String QUESTION_TYPE_TAG = "type";
    private static final String QUESTION_SENTENCE_TAG = "sentence";
    private static final String QUESTION_ANSWER_TAG = "answer";
    private static final String QUESTION_OPTION_TAG = "option";
    private static final String QUESTION_OPTION_IMAGE_TAG = "image";
    private static final String QUESTION_OPTION_TEXT_TAG = "text";
    private static final int NUM_DEFINITION_OPTIONS = 4;

    public static Skill parseSkillFile(InputStream skill) throws SkillFileParserException {
        Document doc;
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = dBuilder.parse(skill);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new SkillFileParserException("Failed to build document from stream.");
        }

        Element root = doc.getDocumentElement();
        assertCorrectTag(root, SKILL_ROOT_TAG);
        return parseSkillNode(root);
    }

    private static Skill parseSkillNode(Node skillNode) throws SkillFileParserException {
        // Parse title
        Node titleNode = ((Element) skillNode).getElementsByTagName(TITLE_TAG).item(0);
        if (titleNode == null) {
            throw new SkillFileParserException("No title node found in skill node.");
        }
        String title = titleNode.getTextContent();

        // Parse each lesson
        ArrayList<Lesson> lessons = new ArrayList<>();
        NodeList nodes = skillNode.getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node lessonNode = nodes.item(i);
            String nodeName = lessonNode.getNodeName();
            if (LESSON_TAG.equals(nodeName)) {
                Lesson lesson = parseLessonNode(lessons.size() + 1, lessonNode);
                lessons.add(lesson);
            }
        }

        return new Skill(title, lessons);
    }

    private static Lesson parseLessonNode(int id, Node lessonNode) throws SkillFileParserException {
        // Parse word list
        Node wordsNode = ((Element) lessonNode).getElementsByTagName(WORDS_TAG).item(0);
        if (wordsNode == null) {
            throw new SkillFileParserException("No words node found in skill node.");
        }
        String words = wordsNode.getTextContent();

        // Parse each question
        ArrayList<Question> questions = new ArrayList<>();
        NodeList nodes = lessonNode.getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node questionNode = nodes.item(i);
            String nodeName = questionNode.getNodeName();
            if (QUESTION_TAG.equals(nodeName)) {
                Question question = parseQuestionNode((Element) questionNode);
                questions.add(question);
            }
        }

        return new Lesson(id, words, questions);
    }

    private static Question parseQuestionNode(Element questionElement) throws SkillFileParserException {
        String typeString = parseTextElementByTag(questionElement, QUESTION_TYPE_TAG);
        QuestionType type = QuestionType.fromString(typeString);
        if (type == null) {
            throw new SkillFileParserException("Unknown question type: " + typeString + ".");
        }

        if (type == QuestionType.WORD_DEFINITION) {
            return parseDefinitionQuestionNode(questionElement);
        }
        else {
            return parseDefaultQuestionNode(questionElement, type);
        }
    }

    private static DefinitionQuestion parseDefinitionQuestionNode(Element questionElement)
            throws SkillFileParserException {
        String sentence = parseTextElementByTag(questionElement, QUESTION_SENTENCE_TAG);
        String answer = parseTextElementByTag(questionElement, QUESTION_ANSWER_TAG);

        ArrayList<String> options = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        NodeList nodes = questionElement.getChildNodes();
        for (int i=0; i<nodes.getLength(); i++) {
            Node optionNode = nodes.item(i);
            String nodeName = optionNode.getNodeName();
            if (QUESTION_OPTION_TAG.equals(nodeName)) {
                String imageName = parseTextElementByTag((Element) optionNode, QUESTION_OPTION_IMAGE_TAG);
                String option = parseTextElementByTag((Element) optionNode, QUESTION_OPTION_TEXT_TAG);
                options.add(option);
                images.add(imageName);
            }
        }
        if (options.size() != NUM_DEFINITION_OPTIONS || images.size() != NUM_DEFINITION_OPTIONS) {
            throw new SkillFileParserException("Definition question does not have the right number of options.");
        }
        return new DefinitionQuestion(options,images, sentence, answer);
    }

    private static Question parseDefaultQuestionNode(Element questionElement, QuestionType type)
        throws SkillFileParserException {
        String sentence = parseTextElementByTag(questionElement, QUESTION_SENTENCE_TAG);
        String answer = parseTextElementByTag(questionElement, QUESTION_ANSWER_TAG);
        return new Question(type, sentence, answer);
    }

    private static String parseTextElementByTag(Element root, String tag)
            throws SkillFileParserException {
        Node textNode = root.getElementsByTagName(tag).item(0);
        if (textNode == null) {
            throw new SkillFileParserException("Couldn't find element" + tag + ".");
        }
        return textNode.getTextContent();
    }

    private static void assertCorrectTag(Node node, String expectedTag) throws SkillFileParserException {
        String actual = node.getNodeName();
        if (expectedTag.equals(actual) == false) {
            throw new SkillFileParserException("Unpexpected tag " + actual + " in place of " + expectedTag +".");
        }
    }
}

