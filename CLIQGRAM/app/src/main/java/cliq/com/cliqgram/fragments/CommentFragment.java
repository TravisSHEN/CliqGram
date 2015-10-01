package cliq.com.cliqgram.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.adapters.CommentAdapter;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POST = "post";

    // TODO: Rename and change types of parameters
    private String postId;

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

        ParseUser currentUser = ParseUser.getCurrentUser();

        User user1 = User.userFactory(currentUser.getUsername(),
                currentUser.getEmail());

        User user2 = User.userFactory("abc",
                "abc@abc.com");

        Comment c1 = Comment.createComment(user1, post, "Good angle");
        Comment c2 = Comment.createComment( user2, post, "Good to hear this");

        commentList.add(c1);
        commentList.add(c2);

        commentAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.comment_send)
    public void onSendClick(View view){

        ParseUser currentUser = ParseUser.getCurrentUser();

        User user = User.userFactory(currentUser.getUsername(),
                currentUser.getEmail());
        String content = commentEdit.getText().toString();

        if( validateComment(content) ) {
            commentEdit.setText("");

            Comment comment = Comment.createComment(user, post, content);
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
