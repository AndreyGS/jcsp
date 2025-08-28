package io.andreygs.jcsp_base.common;

public interface IBufferResizeStrategy
{
    int calculateResize(int currentlyAllocatedSize, int minimumRequiredSize);
}
