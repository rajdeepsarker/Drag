package com.example.drag;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	ListView source = null;
	TextView target1 = null;
	TextView target2 = null;
	TextView target3 = null;
	
	String[] listItems = {"list1", "list2", "list3", "list4"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        source = (ListView)findViewById(R.id.dragSource);
        target1 = (TextView) findViewById(R.id.dragTarget1);
        target2 = (TextView) findViewById(R.id.dragTarget2);
        target3 = (TextView) findViewById(R.id.dragTarget3);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        source.setAdapter(adapter);
        
        // Start Drag
        source.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
        		String item = listItems[position];
        		
        		ClipData data = ClipData.newPlainText("DragIt", item);
        		source.startDrag(data, new MyShadowBuilder(view), null, 0);
			}
		});
        
        // Handle Drag
        target1.setOnDragListener(new MyDragListener());
        target2.setOnDragListener(new MyDragListener());
        target3.setOnDragListener(new MyDragListener());
    }

    // Drag Shadow
    private class MyShadowBuilder extends DragShadowBuilder {

    	public MyShadowBuilder(View v) {
    		super(v);
    	}
    	
		@Override
		public void onDrawShadow(Canvas canvas) {
			// Set Drag image background or anything you want
			int width = getView().getWidth();
			int height = getView().getHeight();
			Paint paint = new Paint();
			paint.setColor(0x55858585);
			
			canvas.drawRect(new Rect(0, 0, width, height), paint);
			
			super.onDrawShadow(canvas);
		}

		@Override
		public void onProvideShadowMetrics(Point shadowSize,
				Point shadowTouchPoint) {
			
			int width = getView().getWidth();
			int height = getView().getHeight();
			
			shadowSize.set(width, height);
			
			shadowTouchPoint.set(width/2, height);
		}
    }
    
    // Drag Listener
    private class MyDragListener implements OnDragListener {
    	private final int DEFAULT_BG_COLOR = 0xFF858585;
    	private final int HIGHLIGHT_BG_COLOR = 0xFF0000FF;
    	
		@Override
		public boolean onDrag(View v, DragEvent event) {
			
			if(event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {
				v.setBackgroundColor(HIGHLIGHT_BG_COLOR);
			}
			else if(event.getAction() == DragEvent.ACTION_DRAG_EXITED) {
				v.setBackgroundColor(DEFAULT_BG_COLOR);
			}
			else if(event.getAction() == DragEvent.ACTION_DROP) {
				// Perform drop
				ClipData clip = event.getClipData();
				ClipData.Item item = clip.getItemAt(0);
				String text = item.getText().toString();
				
				((TextView) v).setText(text);
				v.setBackgroundColor(DEFAULT_BG_COLOR);
			}
			// Send true to listen All Drag Events.
			return true; 
		}
    }
}

