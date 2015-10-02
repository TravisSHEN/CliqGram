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

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.CommentAdapter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
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

        // register with EventBus
        AppStarter.eventBus.register(this);

        commentList = new ArrayList<>();
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
        this.initializeData();

        return root_view;
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

    private void initializeData(){

        Post post = null;

        // TODO: find post by id via post service
//        PostService.getPost( postId );
        PostService.getPost("X8f2UlSJIc");
        ProgressSpinner.getInstance().showSpinner(this.getActivity(),
                "Loading...");

        ParseUser currentUser = ParseUser.getCurrentUser();

        User user1 = User.userFactory(currentUser
                        .getUsername(),
                currentUser.getEmail());

        User user2 = User.userFactory("abc",
                "abc@abc.com");

        Comment c1 = Comment.createComment(user1, post, "Good angle");
        Comment c2 = Comment.createComment( user2, post, "Good to hear this");

        commentList.add(c1);
        commentList.add(c2);

        commentAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onGetPostEvent(GetPostEvent event){

        if(event.isSuccess()){
           this.post = event.getPost();
            this.commentList = post.getCommentList();
            // notify adapter to load data
            commentAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), post.getPostId(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "No data found.", Toast
                    .LENGTH_SHORT).show();
        }
        ProgressSpinner.getInstance().dismissSpinner();
    }

    @OnClick(R.id.comment_send)
    public void onSendClick(View view){

        String content = commentEdit.getText().toString();

        if( validateComment(content) ) {
            commentEdit.setText("");

            Comment comment = Comment.createComment(UserService.getCurrentUser(), post,
                    content);
            commentList.add(comment);

            commentAdapter.notifyDataSetChanged();
        }
    }

    private boolean validateComment(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        return true;
    }
}
