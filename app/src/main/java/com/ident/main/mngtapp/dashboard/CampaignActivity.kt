package com.ident.main.mngtapp.dashboard

import android.os.Bundle
import com.ident.main.mngtapp.R
import com.ident.main.mngtapp.base.BaseActivity
import com.ident.main.mngtapp.databinding.ActivityCampaignBinding

class CampaignActivity : BaseActivity<ActivityCampaignBinding>(ActivityCampaignBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding?.apply {

            toolBar.tvTitle.text = resources.getString(R.string.campaign)

            toolBar.imgBack.setOnClickListener {
                finish()
            }
        }
    }
}