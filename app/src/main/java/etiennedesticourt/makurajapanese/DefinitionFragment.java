package etiennedesticourt.makurajapanese;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import etiennedesticourt.makurajapanese.Skill.DefinitionQuestion;
import etiennedesticourt.makurajapanese.databinding.FragmentQuestionDefinitionBinding;

public class DefinitionFragment extends AnswerFragment {
    private static final String IMAGE_FOLDER = "drawable";

    @Override
    public View bind(LayoutInflater inflater, ViewGroup container) {
        FragmentQuestionDefinitionBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_question_definition, container, false);
        binding.setQuestion((DefinitionQuestion) getQuestion());

        View root = binding.getRoot();
        setOptionImage(root, R.id.image1, 0);
        setOptionImage(root, R.id.image2, 1);
        setOptionImage(root, R.id.image3, 2);
        setOptionImage(root, R.id.image4, 3);

        return root;
    }

    private void setOptionImage(View rootView, int viewId, int imageId) {
        DefinitionQuestion question = (DefinitionQuestion) getQuestion();
        ImageView image = (ImageView) rootView.findViewById(viewId);
        String resourceName = question.getImageResource(imageId);
        int resourceId = getResources().getIdentifier(resourceName, IMAGE_FOLDER, MainActivity.PACKAGE_NAME);
        image.setImageResource(resourceId);
    }
}
