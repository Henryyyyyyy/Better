package me.henry.versatile.base;


import me.henry.versatile.fragment.Mainfragment;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by henry on 2017/11/22.
 */

public abstract class VFragment extends PermissionCheckerFragment{
    public <T extends VFragment> T getParentVFragment() {
        return (T) getParentFragment();
    }
    public void startFragmentFromMain(ISupportFragment fragment) {
        Mainfragment mainfragment = getMainActivity().findFragment(Mainfragment.class);
        mainfragment.start(fragment);
    }

}
