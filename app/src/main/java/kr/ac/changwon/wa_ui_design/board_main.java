package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link board_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class board_main extends Fragment {
    private ListView listView;
    private ListView questionListView;
    private ListView tipListView;
    private ArrayList<BoardWrite> boardList; // 전체글
    private BoardWriteAdapter boardwriteAdapter;
    private ArrayList<BoardWrite> questionList; // 질문만
    private BoardWriteAdapter questionAdapter;
    private ArrayList<BoardWrite> tipList; // 팁만
    private BoardWriteAdapter tipAdapter;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public board_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment board_main.
     */
    // TODO: Rename and change types and number of parameters
    public static board_main newInstance(String param1, String param2) {
        board_main fragment = new board_main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        boardList = new ArrayList<>();
        questionList = new ArrayList<>();
        tipList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_board_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchField = view.findViewById(R.id.board_search);


        // 전체 글
        listView = view.findViewById(R.id.board_listview);
        boardwriteAdapter = new BoardWriteAdapter(getContext(), boardList);
        listView.setAdapter(boardwriteAdapter);


        // 질문만
        questionListView = view.findViewById(R.id.question_list);
        if (questionListView != null) {
            questionAdapter = new BoardWriteAdapter(getContext(), questionList);
            questionListView.setAdapter(questionAdapter);
        }

        // 팁만
        tipListView = view.findViewById(R.id.tip_list);
        if (tipListView != null) {
            tipAdapter = new BoardWriteAdapter(getContext(), tipList);
            tipListView.setAdapter(tipAdapter);
        }

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            BoardWrite selectedBoard = boardList.get(position);
            Intent intent = new Intent(getActivity(), BoardTextPageActivity.class);
            intent.putExtra("boardTitle", selectedBoard.getBoardTitle());
            intent.putExtra("boardText", selectedBoard.getBoardText());
            intent.putExtra("boardDate", selectedBoard.getBoardDate());
            startActivity(intent);
        });

        Button boardWriteB = view.findViewById(R.id.board_writeB); // 글쓰기 버튼
        boardWriteB.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BoardWriteActivity.class);
            startActivityForResult(intent, 1);
        });

        Button boardQuestionB = view.findViewById(R.id.board_questionB); // 질문 게시판 이동 버튼
        boardQuestionB.setOnClickListener(v -> {
            if (questionAdapter != null) {
                questionAdapter.notifyDataSetChanged();
            }
            Intent intent = new Intent(getActivity(), BoardQuestionActivity.class);
            startActivity(intent);
        });

        Button boardTopB = view.findViewById(R.id.board_tipB); // 팁 게시판 이동 버튼
        boardTopB.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BoardTipActivity.class);
            startActivity(intent);
        });

        // 서버에서 게시물 데이터를 가져오는 메서드 호출
        fetchPosts(searchField); // 데이터 로딩
    }

    /**
     * 서버에서 게시물 데이터를 가져오는 메서드
     */
    private void fetchPosts(EditText searchField) {
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        apiService.getPosts().enqueue(new Callback<List<BoardPost>>() {
            @Override
            public void onResponse(Call<List<BoardPost>> call, Response<List<BoardPost>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // BoardPost를 BoardWrite로 변환
                    List<BoardPost> posts = response.body();
                    boardList.clear();

                    for (BoardPost post : posts) {
                        // category_id를 기반으로 게시물 타입 결정
                        boolean isQuestion = post.getCategory_id() == 1; // 질문 카테고리 ID
                        boolean isTip = post.getCategory_id() == 2;     // 팁 카테고리 ID

                        BoardWrite boardWrite = new BoardWrite(
                                post.getTitle(),
                                post.getContent(),
                                getCurrentDate(), // 또는 서버에서 날짜를 받아온다면 그 값을 사용
                                isQuestion,
                                isTip
                        );

                        boardList.add(boardWrite);

                        // 질문 게시물이면 questionList에도 추가
                        if (isQuestion) {
                            questionList.add(boardWrite);
                        }

                        // 팁 게시물이면 tipList에도 추가
                        if (isTip) {
                            tipList.add(boardWrite);
                        }
                    }

                    // 각 어댑터 업데이트
                    if (boardwriteAdapter != null) {
                        boardwriteAdapter.notifyDataSetChanged();
                    }
                    if (questionAdapter != null) {
                        questionAdapter.notifyDataSetChanged();
                    }
                    if (tipAdapter != null) {
                        tipAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "게시물을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoardPost>> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 검색어 가져오기
                String query = s.toString().toLowerCase().trim();

                // 필터링 로직
                ArrayList<BoardWrite> filteredList = new ArrayList<>();
                for (BoardWrite item : boardList) {
                    if (item.getBoardTitle().toLowerCase().contains(query) ||
                            item.getBoardText().toLowerCase().contains(query)) {
                        filteredList.add(item);
                    }
                }
                boardwriteAdapter.updateData(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // 현재 날짜를 문자열로 반환하는 헬퍼 메소드
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            String boardTitle = data.getStringExtra("boardTitle");
            String boardText = data.getStringExtra("boardText");
            String boardDate = data.getStringExtra("boardDate");


            boolean isQuestionBoard = data.getBooleanExtra("isQuestionBoard", false);
            boolean isTipBoard = data.getBooleanExtra("isTipBoard", false);

            // 일반 게시판에 모든 글 추가
            addBoardList(boardTitle, boardText, boardDate, isQuestionBoard, isTipBoard);

            // 질문 게시판에 추가되는 경우
            if (isQuestionBoard) {
                addQuestionList(boardTitle, boardText, boardDate);
            }

            // 팁 게시판에 추가되는 경우
            if (isTipBoard) {
                addTipList(boardTitle, boardText, boardDate);
            }
        }
    }

    public void addBoardList(String boardTitle, String boardText, String boardDate, boolean isQuestionBoard, boolean isTipBoard) {
        // 일반 게시판에 추가될 경우 질문과 팁 여부를 포함하여 객체 생성
        BoardWrite board = new BoardWrite(boardTitle, boardText, boardDate, isQuestionBoard, isTipBoard);
        boardList.add(0,board);

        if (boardwriteAdapter != null) {
            boardwriteAdapter.notifyDataSetChanged();  // 없으면 UI에 표시 안돼
        }
    }

    public void addQuestionList(String boardTitle, String boardText, String boardDate) {
        BoardWrite board = new BoardWrite(boardTitle, boardText, boardDate, true, false);
        questionList.add(0,board);
        if (questionAdapter != null) {
            questionAdapter.notifyDataSetChanged();  // 없으면 ui에 표시 안돼
        }
    }

    public void addTipList(String boardTitle, String boardText, String boardDate) {
        BoardWrite board = new BoardWrite(boardTitle, boardText, boardDate, false, true);
        tipList.add(0,board);
        if (tipAdapter != null) {
            tipAdapter.notifyDataSetChanged();  // 없으면 ui에 표시 안돼
        }
    }
}