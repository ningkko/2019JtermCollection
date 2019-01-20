/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LetterTile extends TextView {

    public static final int TILE_SIZE = 150;
    private Character letter;
    private boolean frozen;

    public LetterTile(Context context, Character letter) {
        super(context);
        this.letter = letter;
        setText(letter.toString());
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setHeight(TILE_SIZE);
        setWidth(TILE_SIZE);
        setTextSize(30);
        setBackgroundColor(Color.rgb(255, 255, 200));
    }

    public void moveToViewGroup(ViewGroup targetView) {
        ViewParent parent = getParent();
        if (parent instanceof StackedLayout ) {
            StackedLayout owner = (StackedLayout) parent;
            owner.pop();
            targetView.addView(this);
            freeze();
            setVisibility(View.VISIBLE);
        } else {
            ViewGroup owner = (ViewGroup) parent;
            owner.removeView(this);
            ((StackedLayout) targetView).push(this);
            unfreeze();
        }
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // if not frozen and the user start touching the view
        if (!this.frozen&&motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            //clipdata encodes metadata about the object. Useful when needs to handle different types of objects
            //dragShadowBuilder allows to control how the View looks like when dragged. A default one is ok.

            startDrag(ClipData.newPlainText("",""),
                    new View.DragShadowBuilder(this),
                    this,
                    0);
            return true;
        }

        return super.onTouchEvent(motionEvent);
    }
}
/**
 DragEvent.ACTION_DRAG_STARTED should highlight the areas that the dragged object can be dropped onto. In this case, we turn the LinearLayouts blue to indicate this (and call invalidate to let the system know to redraw itself).
 DragEvent.ACTION_DRAG_ENTERED should highlight the area differently to indicate the object being dragged will be added to this ViewGroup if it is dropped. We do this by turning the area green.
 DragEvent.ACTION_DRAG_EXITED. If the user enters the area but leaves without dropping the object, we need to reset the area to blue.
 DragEvent.ACTION_DRAG_ENDED should reset the highlights once dragging is done (even if the object was not dropped anywhere that will accept it).
 DragEvent.ACTION_DROP is where the interesting logic happens. When the dragged object is dropped we need to remove it from its former location and add to its new location.
 */