package etiennedesticourt.makurajapanese;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import etiennedesticourt.makurajapanese.Analytics.Logger;
import etiennedesticourt.makurajapanese.Skill.DefinitionQuestion;
import etiennedesticourt.makurajapanese.databinding.FragmentQuestionDefinitionBinding;

public class DefinitionFragment extends AnswerFragment {
    private static final int DEFAULT_DRAWABLE_ID = R.drawable.sound_icon;
    private static final String IMAGE_FOLDER = "drawable";
    private static final String SOUND_FOLDER = "raw";
    private int selectedOption = -1;
    private int selectedId = -1;
    private Logger logger = FirebaseLogger.INSTANCE;

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

    @Override
    public String getAnswer() {
        if (selectedOption == -1) {
            return "";
        }
        DefinitionQuestion question = (DefinitionQuestion) getQuestion();
        return question.getOption(selectedOption);
    }

    @Override
    public void onClick(View v) {
        if (selectedId != -1) {
            View previouslySelected = getView().findViewById(selectedId);
            previouslySelected.setSelected(false);
        }
        selectedOption = Integer.valueOf((String) v.getTag());
        selectedId = v.getId();
        v.setSelected(true);

        DefinitionQuestion question = (DefinitionQuestion) getQuestion();
        String resourceName = question.getImageResource(selectedOption);
        int resourceId = getResources().getIdentifier(resourceName, SOUND_FOLDER, MainActivity.PACKAGE_NAME);
        if (resourceId != 0) {
            MediaPlayer player = MediaPlayer.create(getActivity(), resourceId);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            player.start();
        }

        logger.logQuestionOptionSelectedEvent(getQuestion().getId(), selectedOption, resourceName);
    }

    private void setOptionImage(View rootView, int viewId, int imageId) {
        DefinitionQuestion question = (DefinitionQuestion) getQuestion();
        ImageView image = (ImageView) rootView.findViewById(viewId);
        String resourceName = question.getImageResource(imageId);
        int resourceId = getResources().getIdentifier(resourceName, IMAGE_FOLDER, MainActivity.PACKAGE_NAME);
        if (resourceId == 0) {
            resourceId = DEFAULT_DRAWABLE_ID;
        }
        image.setImageResource(resourceId);
    }

    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}

