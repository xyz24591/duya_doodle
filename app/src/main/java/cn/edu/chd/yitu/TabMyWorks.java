package cn.edu.chd.yitu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import cn.edu.chd.adapter.YiImageAdapter;
import cn.edu.chd.utils.FileCopy;
import cn.edu.chd.utils.YiUtils;
import cn.edu.chd.values.ApplicationValues;

/**
 * @author ZhangQiong
 *         "我的作品"选项卡 生命周期：onCreate->onCreateView
 */
public class TabMyWorks extends Fragment {
    /**
     * 是否有作品
     */
    public boolean has_works;
    private GridView mGridView = null;
    private static final int DEFAULT_WIDTH = 140;//默认宽度
    private static final int DEFAULT_HEIGHT = 170;//默认高度
    private static final String TAG = "TabMyWorks";
    public static final String POSITION = "position";
    /**
     * 作品文件夹下的图片名
     */
    private List<String> imageNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());*/
        super.onCreate(savedInstanceState);
        has_works = getImageNames().size() == 0 ? false : true;//判断指定文件夹中是否有文件存在
        if (has_works) {
            imageNames = getImageNames();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        if (has_works) {
            view = inflater.inflate(R.layout.layout_tab_mine, null);
            mGridView = (GridView) view.findViewById(R.id.grid_view_tab_mine_works);
        } else {
            view = inflater.inflate(R.layout.layout_tab_mine_noworks, null);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (has_works) {
            loadImage();
            mGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //TODO 浏览
                    Intent intent = new Intent(TabMyWorks.this.getActivity(), BrowseWorks.class);
                    intent.putExtra(POSITION, position + "");
                    TabMyWorks.this.startActivity(intent);

                }
            });
            registerForContextMenu(mGridView);//注册上下文菜单
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        //创建上下文菜单
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_works_item_selected, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();//需要向下转型
        switch (item.getItemId()) {
            case R.id.id_menu_works_delete:
                delete(info.position);
                return true;
            case R.id.id_menu_works_edit:
                edit(info.position);
                return true;
            case R.id.id_menu_works_share: //分享
                share(info.position);
            case R.id.id_menu_works_face:
                faceCheck(info.position);
            default:
                return super.onContextItemSelected(item);
        }

    }


    /**
     * 删除作品
     */
    private void delete(int position) {
        String fileName = imageNames.get(position);
        new File(fileName).delete();

        ChooseModel activity = (ChooseModel) getActivity();
        activity.getAdapter().reLoad();
    }
    /**
     * 分享作品
     */
    private void share(int position){



        File fileUri = new File(imageNames.get(position));
        Log.i("dwad",fileUri.toString());
        Uri imgUri = Uri.fromFile(fileUri);

        /*Intent intent=new Intent();
        intent.setClass(getContext(),TtActivity.class);
        //利用bundle来存取数据
        Bundle bundle=new Bundle();
        String fileUri1=fileUri.toString();
        //bundle.putDouble("height",height);
        bundle.putString("fileUri1",fileUri1);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent,0);*/


        Intent shareIntent = new Intent();
        shareIntent.setType("image*//*");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

        startActivity(Intent.createChooser(shareIntent, "分享到"));

    }
    private void faceCheck(int position){
//       int i=FileCopy.CopySdcardFile("file:///android_asset/welcome.jpg", YiUtils.getTempPath()+"/sb.jpg");


        Log.i("adasda","oooo");
//       Log.i("fdsf",i+YiUtils.getTempPath()+"sb.jpg");



        File file = new File(imageNames.get(position));

        Intent intent=new Intent();
        intent.setClass(getContext(),FaceCheckActivity.class);
        //利用bundle来存取数据
        Bundle bundle=new Bundle();
        String fileUri1=file.toString();
        //bundle.putDouble("height",height);
        bundle.putString("fileUri1",fileUri1);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent,0);
        /*ArrayList faceResult = new ArrayList();
        try {
            faceResult = FaceTest.faceTest(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (faceResult.size()==0){
            Toast.makeText(getActivity(),"恐怕这个图片太大了，换一张试试吧",Toast.LENGTH_LONG).show();

        }else if (faceResult.get(0).equals("0")){
            Toast.makeText(getActivity(),"貌似这里没有人类的脸啊??",Toast.LENGTH_LONG).show();
        }else {
        *//*AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());
        dialog.setTitle("ABCD123");
        dialog.setMessage("aaaa");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();*//*
            Toast.makeText(getActivity(), "性别:" + faceResult.get(0) + "\n年龄:" + faceResult.get(1) + "\n颜值分:\n女性角度 " + faceResult.get(2) + "\n男性角度 " + faceResult.get(3), Toast.LENGTH_LONG).show();

        }*/
    }
    /*private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "20180322_092123.jpg", "你对图片的描述");
        } catch (FileNotFoundException e) {
            e.printStackTrace() ;
        }
        return url;
    }*/
    /*private void shareImg(String dlgTitle, String subject, String content,
                          Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (content != null && !"".equals(content)) {
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }

        // 设置弹出框标题
        if (dlgTitle != null && !"".equals(dlgTitle)) { // 自定义标题
            startActivity(Intent.createChooser(intent, dlgTitle));
        } else { // 系统默认标题
            startActivity(intent);
        }
    }*/
    /**
     * 编辑作品
     *
     * @param position
     */
    private void edit(int position) {
        Intent intent = new Intent(getActivity(), CanvasPreview.class);
        intent.putExtra(ApplicationValues.Base.PREVIEW_TYPE, ApplicationValues.Base.TYPE_MY_WORKS);
        intent.putExtra(TabDIY.IMAGE_DATA, imageNames.get(position));
        startActivity(intent);
    }

    /**
     * 获取指定文件夹下的作品图片名称集合
     *
     * @return
     */
    private List<String> getImageNames() {
        String dir = null;
        List<String> imageNames = new ArrayList<String>();//防止null指针
        if (YiUtils.isSDCardAvailable()) {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + ApplicationValues.Base.SAVE_PATH;
            String[] temp1 = YiUtils.traverseImages(dir);
            if (temp1 != null)
                imageNames = new ArrayList<String>(Arrays.asList(temp1));
        }
        dir = Environment.getDataDirectory().getAbsolutePath() + ApplicationValues.Base.SAVE_PATH;
        String[] temp2 = YiUtils.traverseImages(dir);
        if (temp2 != null) {
            for (String str : temp2) {
                imageNames.add(str);
            }
        }
        Log.i(TAG, "---------->GET_IMAGE_NAMES RUN,NUM = " + imageNames.size());
        return imageNames;
    }

    /**
     * 加载图片并显示到GridView上
     */
    private void loadImage() {
        Point point = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        mGridView.setAdapter(new YiImageAdapter(getActivity(), imageNames, mGridView, point));
    }
}





