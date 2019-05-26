package com.example.feedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_CriteriaList extends Activity {

    ProjectInfo project;
    int indexOfProject;
    ArrayList<Criteria> defaultCriteriaList;
    ListView listView_criteriaDefault;
    ListView listView_marketCriteria;
    ListView listView_commentOnly;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__criteria_list);
        System.out.println("criteriaList界面onCreate");
        Intent intent =getIntent();
        indexOfProject = Integer.parseInt(intent.getStringExtra("index"));
        init();
        handler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 0:
                        Intent intent = new Intent(Activity_CriteriaList.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        AllFunctions.getObject().setHandler(handler);

    }

    protected void onNewIntent(Intent intent) {
        System.out.println("criteriaList界面onNewIntent");
        Intent intent2 =getIntent();
        indexOfProject = Integer.parseInt(intent2.getStringExtra("index"));
        init();
        AllFunctions.getObject().setHandler(handler);
    }


    private void init()
    {
        project = AllFunctions.getObject().getProjectList().get(indexOfProject);
        defaultCriteriaList = DefaultCriteriaList.getDefaultCriteriaList();
        defaultCriteriaList.removeAll(project.getCriteria());
        defaultCriteriaList.removeAll(project.getCommentList());
        listView_criteriaDefault = findViewById(R.id.listView_CriteriaList_inCriteriaList);
        listView_marketCriteria = findViewById(R.id.listView_marketCriteria_inCriteriaList);
        listView_commentOnly = findViewById(R.id.listView_commentOnly_inCriteriaList);
        MyAdapter_criteriaListDefault myAdapter1 = new MyAdapter_criteriaListDefault(defaultCriteriaList,this);
        MyAdapter_criteriaListDefault myAdapter2 = new MyAdapter_criteriaListDefault(project.getCriteria(),this);
        MyAdapter_criteriaListDefault myAdapter3 = new MyAdapter_criteriaListDefault(project.getCommentList(),this);
        listView_criteriaDefault.setAdapter(myAdapter1);
        listView_marketCriteria.setAdapter(myAdapter2);
        listView_commentOnly.setAdapter(myAdapter3);
        listView_criteriaDefault.setOnDragListener(dragListenerForDefaultListview);
        listView_marketCriteria.setOnDragListener(dragListenerForMarketCriteriaList);
        listView_commentOnly.setOnDragListener(dragListenerForCommentOnlyCriteria);
        TextView textView_projectName = findViewById(R.id.textView_projectName_CriteriaList);
        textView_projectName.setText(project.getProjectName());
        TextView textView_helloUser = findViewById(R.id.textView_helloUser_criteriaList);
        textView_helloUser.setText("Hello, "+AllFunctions.getObject().getUsername());
        TextView textView_logout = findViewById(R.id.textView_logout_criteriaList);
        textView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_CriteriaList.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    //button back.
    public void back_inCriteriaList(View view)
    {
        finish();
    }

    //button next.
    public void next_inCriteriaList(View view)
    {
        Intent intent = new Intent(this, Activity_MarkAllocation.class);
        intent.putExtra("index", String.valueOf(indexOfProject));
        startActivity(intent);
    }


    public void addMarkedCriteria(View view)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);//获得layoutInflater对象
        final View view2 = layoutInflater.from(this).inflate(R.layout.dialog_add_criteria, null);//获得view对象

        Dialog dialog = new android.app.AlertDialog.Builder(this).setView(view2).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText editText_newCriteriaName = view2.findViewById(R.id.editText_criteriaName_dialogAddCriteria);//获取控件
                String newCriteriaName = editText_newCriteriaName.getText().toString();

                if(findWhichCriteriaList_itbelongs(newCriteriaName) == -999)
                {
                    Criteria criteria = new Criteria();
                    criteria.setName(newCriteriaName);
                    project.getCriteria().add(criteria);
                    init();
                }
                else
                {
                    Toast.makeText(Activity_CriteriaList.this, "Criteria "+newCriteriaName+" has been existed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        }).create();
        dialog.show();
    }

    public void addCommentCriteria(View view)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this);//获得layoutInflater对象
        final View view2 = layoutInflater.from(this).inflate(R.layout.dialog_add_criteria, null);//获得view对象

        Dialog dialog = new android.app.AlertDialog.Builder(this).setView(view2).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText editText_newCriteriaName = view2.findViewById(R.id.editText_criteriaName_dialogAddCriteria);//获取控件
                String newCriteriaName = editText_newCriteriaName.getText().toString();

                if(findWhichCriteriaList_itbelongs(newCriteriaName) == -999)
                {
                    Criteria criteria = new Criteria();
                    criteria.setName(newCriteriaName);
                    project.getCommentList().add(criteria);
                    init();
                }
                else
                {
                    Toast.makeText(Activity_CriteriaList.this, "Criteria "+newCriteriaName+" has been existed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        }).create();
        dialog.show();
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


        View.OnDragListener dragListenerForDefaultListview = new View.OnDragListener()
        {
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

                            v.setBackgroundColor(Color.parseColor("#dbdbdb"));
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
            };

        View.OnDragListener dragListenerForMarketCriteriaList =  new View.OnDragListener() {

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

                        v.setBackgroundColor(Color.parseColor("#dbdbdb"));
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
        };

        View.OnDragListener dragListenerForCommentOnlyCriteria = new View.OnDragListener() {

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

                        v.setBackgroundColor(Color.parseColor("#dbdbdb"));
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
        };


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
