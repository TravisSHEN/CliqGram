package cliq.com.cliqgram.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.CommentAdapter;
import cliq.com.cliqgram.events.CommentReadyEvent;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.CommentService;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserService;
import de.greenrobot.event.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends android.support.v4.app.Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST = "post";

    private String postId;

    private Post post;

    @Bind(R.id.comment_recycler_view)
    RecyclerView commentView;

    @Bind(R.id.comment_edit)
    EditText commentEdit;
    @Bind(R.id.comment_send)
    Button commentSend;

    private CommentAdapter commentAdapter;

    private List<Comment> commentList;
//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postId String.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String postId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST, postId);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST);

        }

        commentList = new ArrayList<>();
        this.initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_comment,
                container, false);

        // bind this fragment
        ButterKnife.bind(this, root_view);

        this.initializeCommentView();

        return root_view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // register with EventBus
        AppStarter.eventBus.register(this);

    }

    @Override
    public void onStop() {
        AppStarter.eventBus.unregister(this);
        super.onStop();
    }


    private void initializeCommentView() {
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.VERTICAL, false);
        commentView.setLayoutManager(llm);
        commentView.setHasFixedSize(true);

        commentAdapter = new CommentAdapter(this.getActivity()
                , commentList);
        commentView.setAdapter(commentAdapter);
    }

    private void initializeData() {

        // TODO: find post by id via post service
        PostService.getPost(postId);
//        PostService.getPost("X8f2UlSJIc");
        ProgressSpinner.getInstance().showSpinner(this.getActivity(),
                "Loading...");
    }

    @Subscribe
    public void onGetPostEvent(GetPostEvent event) {

        if (event.isSuccess()) {
            this.post = event.getPost();
            // check if it's in same post
            if (!this.post.getObjectId().equals(postId)) {
                return;
            }
            this.commentList = post.getCommentList();
            commentAdapter.updateComments(this.commentList);

            Toast.makeText(this.getActivity(), post.getObjectId(), Toast
                    .LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "No data found.", Toast
                    .LENGTH_SHORT).show();
        }
        ProgressSpinner.getInstance().dismissSpinner();
    }

    @Subscribe
    public void onCommentReadyEvent(CommentReadyEvent event) {
        Comment comment = event.getComment();
        // if not belong to this post, then return;
        if (comment == null || ! comment.getPost().getObjectId().equals
                (postId)) {
            Toast.makeText(this.getActivity(), "Not updated", Toast
                    .LENGTH_SHORT).show();
            return;
        }

        // if existing in commentList, then return;
        for(Comment c : commentList){
            if(c.getObjectId().equals(comment.getObjectId())){
                return;
            }
        }

        this.commentList.add(comment);
        commentAdapter.updateComments(this.commentList);
    }

    @OnClick(R.id.comment_send)
    public void onSendClick(View view) {

        String content = commentEdit.getText().toString();

        if (validateComment(content)) {
            commentEdit.setText("");

            Comment comment = Comment.createComment(UserService.getCurrentUser(), post,
                    content);
            CommentService.comment(this.post, comment);
        }
    }

    private boolean validateComment(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        return true;
    }
}
