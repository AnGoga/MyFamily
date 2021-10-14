package com.angogasapps.myfamily.ui.customview

import com.angogasapps.myfamily.utils.showInDevelopingToast
import android.widget.TextView
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.angogasapps.myfamily.R
import android.content.res.TypedArray
import com.angogasapps.myfamily.models.ActionCardState
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.angogasapps.myfamily.databinding.CardviewBinding

class CardView : CardView {
    private var mCardDrawable = 0
    private lateinit var mCardName: String
    private lateinit var mCardSubscript: String
    private lateinit var textName: TextView
    private lateinit var textSubscript: TextView
    private lateinit var imageView: ImageView

    companion object{
        @JvmStatic
        private val plugClickListener = OnClickListener { v: View? -> showInDevelopingToast() }
    }

    constructor(
        context: Context,
        mCardDrawable: Int,
        mCardName: String,
        mCardSubscript: String
    ) : super(context) {
        this.mCardDrawable = mCardDrawable
        this.mCardName = mCardName
        this.mCardSubscript = mCardSubscript
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(context, attrs)
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttribute(context, attrs)
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        @SuppressLint("ServiceCast") val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cardview, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //set attributes
        textName = findViewById(R.id.cardViewTextViewTextName)
        textSubscript = findViewById(R.id.cardViewTextSubscript)
        imageView = findViewById(R.id.cardViewDrawable)
        update(mCardName, mCardSubscript, mCardDrawable)
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardView)

        //init vars
        mCardName = typedArray.getString(R.styleable.CardView_itemCardName)?:""
        mCardSubscript = typedArray.getString(R.styleable.CardView_itemCardSubscript)?:""
        mCardDrawable = typedArray.getResourceId(R.styleable.CardView_itemCardDrawable, 0)
        typedArray.recycle()
    }

    private fun update(name: String?, subscribe: String?, drawable: Int) {
        textName.text = name
        textSubscript.text = subscribe
        imageView.setBackgroundResource(drawable)
    }

    fun update(state: ActionCardState) {
        update(state.name, state.subscript, state.drawable)
        if (state.mClass != null) {
            rootView.setOnClickListener { v: View? ->
                context.startActivity(Intent(context, state.mClass))
            }
        } else {
            rootView.setOnClickListener(plugClickListener)
        }
    }
}