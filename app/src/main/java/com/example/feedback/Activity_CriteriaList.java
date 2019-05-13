package com.example.feedback;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_CriteriaList extends AppCompatActivity {

    ProjectInfo project;
    int indexOfProject;
    ArrayList<Criteria> defaultCriteriaList;
    ListView listView_criteriaDefault;
    ListView listView_marketCriteria;
    ListView listView_commentOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__criteria_list);

        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);

        defaultCriteriaList = DefaultCriteriaList.getDefaultCriteriaList();

        init();

    }

    private void init()
    {
        listView_criteriaDefault = findViewById(R.id.listView_CriteriaList_inCriteriaList);
        listView_marketCriteria = findViewById(R.id.listView_marketCriteria_inCriteriaList);
        listView_commentOnly = findViewById(R.id.listView_commentOnly_inCriteriaList);
        MyAdapter_criteriaListDefault myAdapter1 = new MyAdapter_criteriaListDefault(defaultCriteriaList,this);
        MyAdapter_criteriaListDefault myAdapter2 = new MyAdapter_criteriaListDefault(project.getCriteria(),this);
        MyAdapter_criteriaListDefault myAdapter3 = new MyAdapter_criteriaListDefault(project.getCommentList(),this);
        listView_criteriaDefault.setAdapter(myAdapter1);
        listView_marketCriteria.setAdapter(myAdapter2);
        listView_commentOnly.setAdapter(myAdapter3);
        setListViewListener();


    }















    public class MyAdapter_criteriaListDefault extends BaseAdapter {
        private Context mContext;
        private ArrayList<Criteria> criteriaList;

        public MyAdapter_criteriaListDefault(ArrayList<Criteria> criteriaListList, Context context) {
            this.criteriaList = criteriaListList;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return criteriaList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_criterialist_default, parent, false);
            TextView textView_criteriaName =  convertView.findViewById(R.id.textView_criterialistDefault_inCriteriaList);
            textView_criteriaName.setText(criteriaList.get(position).getName());

            final View dragView = convertView;
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // DND框架要求传递的数据
                    ClipData.Item item1 = new ClipData.Item(criteriaList.get(position).getName());
                    ClipData.Item item2 = new ClipData.Item(String.valueOf(position));

                    ClipData clipData = new ClipData("", new String[] {
                            ClipDescription.MIMETYPE_TEXT_PLAIN
                    }, item1);
                    clipData.addItem(item2);

                    // 开始当前View的拖动操作，将当前拖动对象的position当作localState传递到拖动事件中
                    dragView.startDrag(clipData, new View.DragShadowBuilder(dragView), null, 0);
                    return true;
                }
            });

            return convertView;
        }
    }

    public void setListViewListener()
    {

        listView_criteriaDefault.setOnDragListener(new View.OnDragListener() {

                @Override
                public boolean onDrag(View v, DragEvent event) {

                    // Defines a variable to store the action type for the incoming event
                    final int action = event.getAction();
                    System.out.println("default listView ondrag listener启动");

                    // Handles each of the expected events
                    switch(action) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // Determines if this View can accept the dragged data
                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                v.invalidate();
                                return true;
                            }
                            return false;
                        case DragEvent.ACTION_DRAG_ENTERED:

                            v.setBackgroundColor(Color.GREEN);
                            v.invalidate();

                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundColor(Color.TRANSPARENT);
                            v.invalidate();
                            return true;

                        case DragEvent.ACTION_DROP:

                            v.setBackgroundColor(Color.TRANSPARENT);
                            // Gets the item containing the dragged data
                            ClipData.Item item1 = event.getClipData().getItemAt(0);
                            ClipData.Item item2 = event.getClipData().getItemAt(1);
                            String source_criteriaName = item1.getText().toString();
                            int source_criteriaIndex = Integer.parseInt(item2.getText().toString());


                            int whichList = findWhichCriteriaList_itbelongs(source_criteriaName);
                            switch (whichList)
                            {
                                case 0:
                                    break;
                                case 1:
                                    Criteria criteria_Temporary = project.getCriteria().get(source_criteriaIndex);
                                    project.getCriteria().remove(source_criteriaIndex);
                                    defaultCriteriaList.add(criteria_Temporary);
                                    break;
                                case 2:
                                    Criteria criteria_Temporary2 = project.getCommentList().get(source_criteriaIndex);
                                    project.getCommentList().remove(source_criteriaIndex);
                                    defaultCriteriaList.add(criteria_Temporary2);
                                    break;
                                default:
                                    ;
                            }

                            init();

                            // Invalidates the view to force a redraw
                            v.invalidate();

                            // Returns true. DragEvent.getResult() will return true.
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            // Turns off any color tinting
                            // Invalidates the view to force a redraw
                            v.invalidate();

                            // returns true; the value is ignored.
                            return true;
                        // An unknown action type was received.
                        default:
                            Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                            break;
                    }
                    return false;
                }

            });

        listView_marketCriteria.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {

                // Defines a variable to store the action type for the incoming event
                final int action = event.getAction();

                // Handles each of the expected events
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            v.invalidate();
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        v.setBackgroundColor(Color.GREEN);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        v.setBackgroundColor(Color.TRANSPARENT);
                        // Gets the item containing the dragged data
                        ClipData.Item item1 = event.getClipData().getItemAt(0);
                        ClipData.Item item2 = event.getClipData().getItemAt(1);
                        String source_criteriaName = item1.getText().toString();
                        int source_criteriaIndex = Integer.parseInt(item2.getText().toString());


                        int whichList = findWhichCriteriaList_itbelongs(source_criteriaName);
                        switch (whichList)
                        {
                            case 0:
                                Criteria criteria_Temporary = defaultCriteriaList.get(source_criteriaIndex);
                                defaultCriteriaList.remove(source_criteriaIndex);
                                project.getCriteria().add(criteria_Temporary);
                                break;
                            case 1:
                                break;
                            case 2:
                                Criteria criteria_Temporary2 = project.getCommentList().get(source_criteriaIndex);
                                project.getCommentList().remove(source_criteriaIndex);
                                project.getCriteria().add(criteria_Temporary2);
                                break;
                            default:
                                ;
                        }

                        init();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Returns true. DragEvent.getResult() will return true.
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        // Turns off any color tinting
                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                        break;
                }
                return false;
            }
        });

        listView_commentOnly.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {

                // Defines a variable to store the action type for the incoming event
                final int action = event.getAction();

                // Handles each of the expected events
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        // Determines if this View can accept the dragged data
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            v.invalidate();
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        v.setBackgroundColor(Color.GREEN);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:

                        v.setBackgroundColor(Color.TRANSPARENT);
                        // Gets the item containing the dragged data
                        ClipData.Item item1 = event.getClipData().getItemAt(0);
                        ClipData.Item item2 = event.getClipData().getItemAt(1);
                        String source_criteriaName = item1.getText().toString();
                        int source_criteriaIndex = Integer.parseInt(item2.getText().toString());


                        int whichList = findWhichCriteriaList_itbelongs(source_criteriaName);
                        switch (whichList)
                        {
                            case 0:
                                Criteria criteria_Temporary = defaultCriteriaList.get(source_criteriaIndex);
                                defaultCriteriaList.remove(source_criteriaIndex);
                                project.getCommentList().add(criteria_Temporary);
                                break;
                            case 1:
                                Criteria criteria_Temporary2 = project.getCriteria().get(source_criteriaIndex);
                                project.getCriteria().remove(source_criteriaIndex);
                                project.getCommentList().add(criteria_Temporary2);
                                break;
                            case 2:
                                break;
                            default:
                                ;
                        }

                        init();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Returns true. DragEvent.getResult() will return true.
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        // Turns off any color tinting
                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // returns true; the value is ignored.
                        return true;
                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                        break;
                }
                return false;
            }
        });



    }

    //0 means defaultCriteriaList, 1 means market criteriaList, 2 means commentOnly criteriaList
    private int findWhichCriteriaList_itbelongs(String criteriaName)
    {
        for(Criteria c : defaultCriteriaList)
        {
            if(c.getName().equals(criteriaName))
                return 0;
        }
        for(Criteria c : project.getCriteria())
        {
            if(c.getName().equals(criteriaName))
                return 1;
        }
        for(Criteria c : project.getCommentList())
        {
            if(c.getName().equals(criteriaName))
                return 2;
        }
        return -999;
    }




}
